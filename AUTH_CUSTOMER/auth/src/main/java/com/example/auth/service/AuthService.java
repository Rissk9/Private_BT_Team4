package com.example.auth.service;

import com.example.auth.dto.CreateEmployeeRequestDto;
import com.example.auth.dto.EmployeeResponseDto;
import com.example.auth.dto.LoginRequestDto;
import com.example.auth.dto.LoginResponseDto;
import com.example.auth.dto.RegisterRequestDto;
import com.example.auth.dto.ResetPasswordRequestDto;
import com.example.auth.dto.UserResponseDto;
import com.example.auth.dto.UserUpdateRequestDto;

public interface AuthService {

    void register(RegisterRequestDto request);

    boolean verifyRegistrationOtp(String identifier, String otp);
    
//    String login(LoginRequestDto request);
    
    LoginResponseDto login(LoginRequestDto request, String ipAddress, String userAgent);
    
    LoginResponseDto refreshToken(String refreshToken);
    
    void forgotPassword(String identifier);
    
    void resetPassword(ResetPasswordRequestDto request);
    
    void logout(String token, String username);
    
    

    UserResponseDto getUserProfile(String username);
    UserResponseDto updateUserProfile(String username, UserUpdateRequestDto request);

    EmployeeResponseDto createEmployee(String callerUsername, CreateEmployeeRequestDto request);
}