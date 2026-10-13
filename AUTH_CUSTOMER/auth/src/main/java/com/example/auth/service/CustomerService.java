package com.example.auth.service;

import com.example.auth.dto.CustomerProfileRequestDto;
import com.example.auth.dto.CustomerProfileResponseDto;
import com.example.auth.dto.OpenAccountRequestDto;
import com.example.auth.dto.OpenAccountResponseDto;

public interface CustomerService {
    CustomerProfileResponseDto submitKycProfile(CustomerProfileRequestDto request, String username);
    CustomerProfileResponseDto getCustomerProfile(String username);
    String getCustomerStatus(String username);
    java.util.List<CustomerProfileResponseDto> getAllCustomers(String username);
    OpenAccountResponseDto openAccount(OpenAccountRequestDto request, String username);
    java.util.List<OpenAccountResponseDto> getAccounts(String username);
}
