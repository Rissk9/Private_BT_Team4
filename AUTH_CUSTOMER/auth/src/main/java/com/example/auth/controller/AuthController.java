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

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequestDto request) {

        authService.register(request);

        return ResponseEntity.ok(
                "Registration request received. OTP sent."
        );
    }

    @PostMapping("/verify-otp")
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
                request.getOtp()
        );

        if (!verified) {
            return ResponseEntity.badRequest()
                    .body("Invalid or expired OTP");
        }

        return ResponseEntity.ok(
                "Registration completed successfully"
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto request) {

        LoginResponseDto response = authService.login(request);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refreshToken(
            @RequestBody RefreshTokenRequestDto request) {

        LoginResponseDto response =
                authService.refreshToken(request.getRefreshToken());

        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequestDto request){
    	
    	authService.forgotPassword(request.getIdentifier());
    	return new ResponseEntity<>("Accepted",HttpStatus.OK);
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetPasswordRequestDto request) {

        authService.resetPassword(request);

        return ResponseEntity.ok(
                "Password reset successfully"
        );
    }
}