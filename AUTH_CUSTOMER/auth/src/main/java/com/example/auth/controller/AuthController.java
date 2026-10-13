package com.example.auth.controller;

import com.example.auth.dto.ForgotPasswordRequestDto;
import com.example.auth.dto.LoginRequestDto;
import com.example.auth.dto.LoginResponseDto;
import com.example.auth.dto.RefreshTokenRequestDto;
import com.example.auth.dto.RegisterRequestDto;
import com.example.auth.dto.ResetPasswordRequestDto;
import com.example.auth.dto.VerifyOtpRequestDto;
import com.example.auth.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.javapoet.LordOfTheStrings.ReturnBuilderSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication and registration APIs")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new customer",
               description = "Initiates the registration process for a new customer. An OTP is sent to the provided email or phone number for verification.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registration request received. OTP sent."),
        @ApiResponse(responseCode = "400", description = "Invalid request body.")
    })
    public ResponseEntity<String> register(
            @RequestBody RegisterRequestDto request) {

        authService.register(request);

        return ResponseEntity.ok(
                "Registration request received. OTP sent.");
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP and complete registration",
               description = "Verifies the one-time password sent during registration and completes the registration process.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registration completed successfully"),
        @ApiResponse(responseCode = "400", description = "Email or phone number is required / Invalid or expired OTP")
    })
    public ResponseEntity<String> verifyOtp(
            @RequestBody VerifyOtpRequestDto request) {

        String identifier;

        if (request.getEmail() != null
                && !request.getEmail().isBlank()) {

            identifier = request.getEmail()
                    .trim()
                    .toLowerCase();

        } else if (request.getPhoneNumber() != null
                && !request.getPhoneNumber().isBlank()) {

            identifier = request.getPhoneNumber().trim();

        } else {

            return ResponseEntity.badRequest()
                    .body("Email or phone number is required");
        }

        boolean verified = authService.verifyRegistrationOtp(
                identifier,
                request.getOtp());

        if (!verified) {
            return ResponseEntity.badRequest()
                    .body("Invalid or expired OTP");
        }

        return ResponseEntity.ok(
                "Registration completed successfully");
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and obtain JWT tokens",
               description = "Authenticates the user with username and password. On success, returns an access token and a refresh token.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful. Returns access and refresh tokens."),
        @ApiResponse(responseCode = "401", description = "Invalid credentials.")
    })
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto request,
            jakarta.servlet.http.HttpServletRequest httpRequest) {

        String ipAddress = httpRequest.getRemoteAddr();
        String userAgent = httpRequest.getHeader("User-Agent");

        LoginResponseDto response = authService.login(request, ipAddress, userAgent);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token",
               description = "Uses the refresh token to obtain a new access token.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "New access token and refresh token returned."),
        @ApiResponse(responseCode = "400", description = "Invalid refresh token.")
    })
    public ResponseEntity<LoginResponseDto> refreshToken(
            @RequestBody RefreshTokenRequestDto request) {

        LoginResponseDto response = authService.refreshToken(request.getRefreshToken());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Initiate password reset",
               description = "Sends a password reset link/code to the provided identifier (email or phone number).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Password reset instructions sent."),
        @ApiResponse(responseCode = "400", description = "Invalid request body.")
    })
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequestDto request) {

        authService.forgotPassword(request.getIdentifier());
        return new ResponseEntity<>("Accepted", HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password",
               description = "Resets the user password using the reset token and new password provided.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Password reset successfully."),
        @ApiResponse(responseCode = "400", description = "Invalid or expired reset token.")
    })
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetPasswordRequestDto request) {

        authService.resetPassword(request);

        return ResponseEntity.ok(
                "Password reset successfully");
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user",
               description = "Invalidates the current access token and refresh token. Requires JWT authentication.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Logged out successfully."),
        @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid or missing JWT.")
    })
    public ResponseEntity<String> logout(
            jakarta.servlet.http.HttpServletRequest httpRequest,
            org.springframework.security.core.Authentication authentication) {

        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = authentication.getName();
            authService.logout(token, username);
        }

        return ResponseEntity.ok("Logged out successfully");
    }
}