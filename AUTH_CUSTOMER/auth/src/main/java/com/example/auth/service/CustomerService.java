package com.example.auth.service;

import com.example.auth.dto.CustomerProfileRequestDto;
import com.example.auth.dto.CustomerProfileResponseDto;

public interface CustomerService {
    CustomerProfileResponseDto submitKycProfile(CustomerProfileRequestDto request, String username);
}
