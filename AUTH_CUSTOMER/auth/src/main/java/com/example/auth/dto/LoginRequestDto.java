package com.example.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LoginRequestDto {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 150, message = "Username (email) must be 3-150 characters")
    @Pattern(
        regexp = "^[a-z0-9]+(\\.[a-z0-9]+)*@[a-z0-9]+(\\.[a-z0-9]+)+$",
        message = "Username must be a valid lowercase email")
    @Schema(example = "example@example.com", description = "Username is email")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 7, message = "Password must be at least 7 characters")
    @Pattern(
        regexp = "(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-\\[\\]{};\\\\':\\\"\\\\\\\\|,.<>/?]).{7,}",
        message = "Password must contain at least 1 uppercase, 1 lowercase, 1 digit, 1 special character, and be at least 7 characters")
    @Schema(example = "Test1@a", description = "At least 7 chars with 1 uppercase, 1 lowercase, 1 digit, 1 special char")
    private String password;

    public LoginRequestDto() {
    }

    public LoginRequestDto(String username, String password) {
        this.username = username != null ? username.trim() : null;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username != null ? username.trim() : null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}