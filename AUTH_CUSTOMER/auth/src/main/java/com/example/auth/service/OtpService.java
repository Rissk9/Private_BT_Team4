package com.example.auth.service;

public interface OtpService {

    void generateAndSendOtp(String identifier);

    boolean verifyOtp(String identifier, String enteredOtp);
    
}