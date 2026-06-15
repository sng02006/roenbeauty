package com.example.roenbeauty.global.dto;

import com.example.roenbeauty.user.enums.UserRole;

public class AuthUser {

    private final Long userId;
    private final String email;
    private final UserRole role;

    public AuthUser(
            Long userId,
            String email,
            UserRole role
    ) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }
}