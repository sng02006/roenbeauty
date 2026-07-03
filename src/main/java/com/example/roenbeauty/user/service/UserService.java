package com.example.roenbeauty.user.service;

import com.example.roenbeauty.global.dto.AuthUser;
import com.example.roenbeauty.global.security.JwtProvider;
import com.example.roenbeauty.reservation.entity.Reservation;
import com.example.roenbeauty.reservation.repository.ReservationRepository;
import com.example.roenbeauty.user.client.KakaoClient;
import com.example.roenbeauty.user.dto.KakaoLoginRequestDto;
import com.example.roenbeauty.user.dto.KakaoTokenResponseDto;
import com.example.roenbeauty.user.dto.KakaoUserResponseDto;
import com.example.roenbeauty.user.dto.LoginRequestDto;
import com.example.roenbeauty.user.dto.LoginResponseDto;
import com.example.roenbeauty.user.dto.PasswordChangeRequestDto;
import com.example.roenbeauty.user.dto.SignupRequestDto;
import com.example.roenbeauty.user.dto.UserInfoResponseDto;
import com.example.roenbeauty.user.entity.User;
import com.example.roenbeauty.user.enums.OAuthProvider;
import com.example.roenbeauty.user.enums.UserRole;
import com.example.roenbeauty.user.repository.UserRepository;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final KakaoClient kakaoClient;
    private final ReservationRepository reservationRepository;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider,
            KakaoClient kakaoClient,
            ReservationRepository reservationRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.kakaoClient = kakaoClient;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public LoginResponseDto signup(SignupRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        if (requestDto.getPassword().length() < 8) {
            throw new IllegalArgumentException("비밀번호는 8자 이상 입력해주세요.");
        }

        User user = new User(
                requestDto.getEmail(),
                passwordEncoder.encode(requestDto.getPassword()),
                requestDto.getName(),
                requestDto.getPhone(),
                UserRole.CUSTOMER,
                OAuthProvider.LOCAL,
                null
        );

        User savedUser = userRepository.save(user);
        String accessToken = jwtProvider.createAccessToken(savedUser);

        return LoginResponseDto.from(savedUser, accessToken);
    }

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByEmailAndProvider(requestDto.getEmail(), OAuthProvider.LOCAL)
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        String accessToken = jwtProvider.createAccessToken(user);

        return LoginResponseDto.from(user, accessToken);
    }

    @Transactional
    public LoginResponseDto kakaoLogin(KakaoLoginRequestDto requestDto) {
        KakaoTokenResponseDto tokenResponse =
                kakaoClient.getToken(requestDto.getCode(), requestDto.getRedirectUri());

        KakaoUserResponseDto kakaoUser =
                kakaoClient.getUserInfo(tokenResponse.getAccessToken());

        String providerId = String.valueOf(kakaoUser.getId());

        KakaoUserResponseDto.KakaoAccount kakaoAccount = kakaoUser.getKakaoAccount();

        String tempName = "카카오 사용자";
        if (kakaoAccount != null
                && kakaoAccount.getProfile() != null
                && kakaoAccount.getProfile().getNickname() != null
                && !kakaoAccount.getProfile().getNickname().isBlank()) {
            tempName = kakaoAccount.getProfile().getNickname();
        }

        final String name = tempName;

        String email = "kakao_" + providerId + "@kakao.local";

        User user = userRepository.findByProviderAndProviderId(OAuthProvider.KAKAO, providerId)
                .orElseGet(() -> userRepository.save(new User(
                        email,
                        null,
                        name,
                        null,
                        UserRole.CUSTOMER,
                        OAuthProvider.KAKAO,
                        providerId
                )));

        String accessToken = jwtProvider.createAccessToken(user);

        return LoginResponseDto.from(user, accessToken);
    }

    @Transactional
    public void withdraw(AuthUser authUser) {
        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        if (user.isDeleted()) {
            throw new IllegalArgumentException("이미 탈퇴한 회원입니다.");
        }

        List<Reservation> reservations =
                reservationRepository.findAllByUser_Id(user.getId());

        reservations.forEach(reservation -> reservation.anonymizeCustomerInfo());

        if (user.getProvider() == OAuthProvider.KAKAO
                && user.getProviderId() != null) {
            kakaoClient.unlinkByAdminKey(user.getProviderId());
        }

        user.withdraw();
    }

    @Transactional
    public void kakaoUnlinkCallback(String providerId) {
        User user = userRepository
                .findByProviderAndProviderIdAndDeletedFalse(
                        OAuthProvider.KAKAO,
                        providerId
                )
                .orElse(null);

        if (user == null) {
            return;
        }

        List<Reservation> reservations =
                reservationRepository.findAllByUser_Id(user.getId());

        reservations.forEach(reservation -> reservation.anonymizeCustomerInfo());

        user.withdraw();
    }

    @Transactional(readOnly = true)
    public UserInfoResponseDto getMyInfo(AuthUser authUser) {
        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        return UserInfoResponseDto.from(user);
    }

    @Transactional
    public void changePassword(
            AuthUser authUser,
            PasswordChangeRequestDto requestDto
    ) {
        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        if (user.getProvider() != OAuthProvider.LOCAL) {
            throw new IllegalArgumentException("현재 로그인 방식에서는 비밀번호를 변경할 수 없습니다.");
        }

        if (!requestDto.getNewPassword().equals(requestDto.getNewPasswordConfirm())) {
            throw new IllegalArgumentException("새 비밀번호가 일치하지 않습니다.");
        }

        if (requestDto.getNewPassword().length() < 8) {
            throw new IllegalArgumentException("비밀번호는 8자 이상 입력해주세요.");
        }

        if (requestDto.getNewPassword().equals(requestDto.getCurrentPassword())) {
            throw new IllegalArgumentException("기존 비밀번호와 동일한 비밀번호는 사용할 수 없습니다.");
        }

        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }

        user.changePassword(
                passwordEncoder.encode(requestDto.getNewPassword())
        );
    }
}