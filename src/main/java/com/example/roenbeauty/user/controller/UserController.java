package com.example.roenbeauty.user.controller;

import com.example.roenbeauty.global.dto.AuthUser;
import com.example.roenbeauty.user.dto.KakaoLoginRequestDto;
import com.example.roenbeauty.user.dto.LoginRequestDto;
import com.example.roenbeauty.user.dto.LoginResponseDto;
import com.example.roenbeauty.user.dto.PasswordChangeRequestDto;
import com.example.roenbeauty.user.dto.SignupRequestDto;
import com.example.roenbeauty.user.dto.UserInfoResponseDto;
import com.example.roenbeauty.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public LoginResponseDto signup(@RequestBody SignupRequestDto requestDto) {
        return userService.signup(requestDto);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto requestDto) {
        return userService.login(requestDto);
    }

    @PostMapping("/kakao/login")
    public LoginResponseDto kakaoLogin(@RequestBody KakaoLoginRequestDto requestDto) {
        return userService.kakaoLogin(requestDto);
    }

    @GetMapping("/me")
    public UserInfoResponseDto getMyInfo(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        return userService.getMyInfo(authUser);
    }

    @PatchMapping("/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody PasswordChangeRequestDto requestDto
    ) {
        userService.changePassword(authUser, requestDto);
    }

    @PostMapping(
            value = "/kakao/unlink",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void kakaoUnlinkCallback(
            @RequestParam("user_id") String userId
    ) {
        userService.kakaoUnlinkCallback(userId);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void withdraw(
            @AuthenticationPrincipal AuthUser authUser
    ) {
        userService.withdraw(authUser);
    }
}