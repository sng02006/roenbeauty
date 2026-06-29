package com.example.roenbeauty.user.dto;

import com.example.roenbeauty.user.entity.User;
import com.example.roenbeauty.user.enums.OAuthProvider;
import java.time.LocalDateTime;

public class UserInfoResponseDto {

    private Long userId;
    private String email;
    private String name;
    private String phone;
    private OAuthProvider provider;
    private LocalDateTime createdAt;

    public UserInfoResponseDto(
            Long userId,
            String email,
            String name,
            String phone,
            OAuthProvider provider,
            LocalDateTime createdAt
    ) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.provider = provider;
        this.createdAt = createdAt;
    }

    public static UserInfoResponseDto from(User user) {
        return new UserInfoResponseDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPhone(),
                user.getProvider(),
                user.getCreatedAt()
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

    public String getPhone() {
        return phone;
    }

    public OAuthProvider getProvider() {
        return provider;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}