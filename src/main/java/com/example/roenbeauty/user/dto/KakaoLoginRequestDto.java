package com.example.roenbeauty.user.dto;

public class KakaoLoginRequestDto {

    private String code;
    private String redirectUri;

    public KakaoLoginRequestDto() {
    }

    public String getCode() {
        return code;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
}