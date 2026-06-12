package com.example.roenbeauty.user.service;

import com.example.roenbeauty.user.dto.LoginRequestDto;
import com.example.roenbeauty.user.dto.LoginResponseDto;
import com.example.roenbeauty.user.dto.SignupRequestDto;
import com.example.roenbeauty.user.entity.User;
import com.example.roenbeauty.user.enums.OAuthProvider;
import com.example.roenbeauty.user.enums.UserRole;
import com.example.roenbeauty.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public LoginResponseDto signup(SignupRequestDto requestDto) {
        if (userRepository.existsByEmailAndProvider(requestDto.getEmail(), OAuthProvider.LOCAL)) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
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

        return LoginResponseDto.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByEmailAndProvider(requestDto.getEmail(), OAuthProvider.LOCAL)
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return LoginResponseDto.from(user);
    }
}