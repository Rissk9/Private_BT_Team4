package com.example.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RefreshTokenRequestDto {

    @NotBlank(message = "Refresh token is required")
    @Size(min = 10, message = "Refresh token must be at least 10 characters")
    private String refreshToken;

    public RefreshTokenRequestDto() {
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}