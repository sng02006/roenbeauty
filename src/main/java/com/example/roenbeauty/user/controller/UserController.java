package com.example.roenbeauty.user.controller;

import com.example.roenbeauty.user.dto.LoginRequestDto;
import com.example.roenbeauty.user.dto.LoginResponseDto;
import com.example.roenbeauty.user.dto.SignupRequestDto;
import com.example.roenbeauty.user.service.UserService;
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
}