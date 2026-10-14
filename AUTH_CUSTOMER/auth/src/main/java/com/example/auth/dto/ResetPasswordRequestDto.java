package com.example.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public class ResetPasswordRequestDto {

    @NotBlank(message = "Identifier is required")
    private String identifier;

    @NotBlank(message = "OTP is required")
    @Size(min = 4, max = 10, message = "OTP must be 4-10 characters")
    private String otp;

    @NotBlank(message = "New password is required")
    @Size(min = 7, message = "Password must be at least 7 characters")
    @Pattern(
        regexp = "(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-\\[\\]{};\\\\':\\\"\\\\\\\\|,.<>/?]).{7,}",
        message = "Password must contain at least 1 uppercase, 1 lowercase, 1 digit, 1 special character, and be at least 7 characters")
    @Schema(example = "NewP@ss1", description = "At least 7 chars with 1 uppercase, 1 lowercase, 1 digit, 1 special char")
    private String newPassword;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    public ResetPasswordRequestDto() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier != null ? identifier.trim() : null;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp != null ? otp.trim() : null;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}