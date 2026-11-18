package com.example.androidmedical.model;

public class LoginData {
    private String accessToken;
    private String refreshToken;
    private String refreshExpiresAt;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getRefreshExpiresAt() {
        return refreshExpiresAt;
    }
}
