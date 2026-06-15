package com.example.roenbeauty.user.enums;

public enum UserRole {

    CUSTOMER,
    OWNER,
    ADMIN;

    public static UserRole of(String role) {

        for (UserRole userRole : values()) {

            if (userRole.name().equalsIgnoreCase(role)) {
                return userRole;
            }
        }

        throw new IllegalArgumentException("유효하지 않은 권한입니다.");
    }
}