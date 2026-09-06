package com.example.auth.service;

import com.example.auth.dto.ForgotPasswordRequestDto;
import com.example.auth.dto.LoginRequestDto;
import com.example.auth.dto.LoginResponseDto;
import com.example.auth.dto.RegisterRequestDto;
import com.example.auth.dto.ResetPasswordRequestDto;

public interface AuthService {

    void register(RegisterRequestDto request);

    boolean verifyRegistrationOtp(String identifier, String otp);
    
//    String login(LoginRequestDto request);
    
    LoginResponseDto login(LoginRequestDto request);
    
    LoginResponseDto refreshToken(String refreshToken);
    
    void forgotPassword(String identifier);
    
    void resetPassword(ResetPasswordRequestDto request);
    
    

}