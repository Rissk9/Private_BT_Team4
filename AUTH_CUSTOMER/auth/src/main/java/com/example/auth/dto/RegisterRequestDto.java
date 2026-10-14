package com.example.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public class RegisterRequestDto {

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Pattern(
        regexp = "^[a-z0-9]+(\\.[a-z0-9]+)*@[a-z0-9]+(\\.[a-z0-9]+)+$",
        message = "Email must contain @, dots before and after @, lowercase only, no spaces or special characters")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    @Schema(example = "emai@example.com", description = "Valid lowercase email")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 20, message = "Phone number must be 10-20 characters")
    @Schema(example = "+911234567890", description = "Phone number with country code")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Size(min = 7, message = "Password must be at least 7 characters")
    @Pattern(
        regexp = "(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};\\\\':\\\"\\\\\\\\|,.<>/?]).{7,}",
        message = "Password must contain at least 1 uppercase, 1 lowercase, 1 digit, 1 special character, and be at least 7 characters")
    @Schema(example = "Test1@a", description = "At least 7 chars with 1 uppercase, 1 lowercase, 1 digit, 1 special char")
    private String password;

    public RegisterRequestDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name.trim() : null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email != null ? email.trim().toLowerCase() : null;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber != null ? phoneNumber.trim() : null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}