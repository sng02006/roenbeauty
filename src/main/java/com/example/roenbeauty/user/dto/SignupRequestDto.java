package com.example.roenbeauty.user.dto;

public class SignupRequestDto {

    private String email;
    private String password;
    private String name;
    private String phone;

    public SignupRequestDto() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}