package com.example.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public class VerifyOtpRequestDto {

    @NotBlank(message = "Email is required when phone number is empty")
    @Pattern(
        regexp = "^[a-z0-9]+(\\.[a-z0-9]+)*@[a-z0-9]+(\\.[a-z0-9]+)+$",
        message = "Email must contain @, dots before and after @, lowercase only, no spaces or special characters")
    @Schema(example = "emai@example.com", description = "Valid lowercase email")
    private String email;

    @NotBlank(message = "Phone number is required when email is empty")
    @Size(min = 10, max = 20, message = "Phone number must be 10-20 characters")
    @Schema(example = "+911234567890", description = "Phone number with country code")
    private String phoneNumber;

    @NotBlank(message = "OTP is required")
    @Size(min = 4, max = 10, message = "OTP must be 4-10 characters")
    private String otp;

    public VerifyOtpRequestDto() {
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

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp != null ? otp.trim() : null;
    }
}