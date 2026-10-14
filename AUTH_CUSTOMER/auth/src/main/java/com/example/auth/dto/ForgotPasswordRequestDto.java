package com.example.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ForgotPasswordRequestDto {

    @NotBlank(message = "Identifier (email or phone) is required")
    @Size(min = 3, max = 150, message = "Identifier must be 3-150 characters")
    private String identifier;

    public ForgotPasswordRequestDto() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier != null ? identifier.trim() : null;
    }
}