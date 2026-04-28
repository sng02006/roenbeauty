package com.example.roenbeauty.user.dto;

import com.example.roenbeauty.user.entity.User;
import com.example.roenbeauty.user.enums.UserRole;

public class LoginResponseDto {

    private Long userId;
    private String email;
    private String name;
    private UserRole role;
    private String phone;

    public LoginResponseDto(Long userId, String email, String name, UserRole role, String phone) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.role = role;
        this.phone = phone;
    }

    public static LoginResponseDto from(User user) {
        return new LoginResponseDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getPhone()
        );
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public UserRole getRole() {
        return role;
    }
    public String getPhone() {
        return phone;
    }
}