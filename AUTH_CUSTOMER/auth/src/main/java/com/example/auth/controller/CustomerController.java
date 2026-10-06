package com.example.auth.controller;

import com.example.auth.dto.CustomerProfileRequestDto;
import com.example.auth.dto.CustomerProfileResponseDto;
import com.example.auth.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/profile")
    public ResponseEntity<CustomerProfileResponseDto> submitProfile(
            @RequestBody CustomerProfileRequestDto request,
            Authentication authentication) {
        
        String username = authentication.getName();
        CustomerProfileResponseDto response = customerService.submitKycProfile(request, username);
        
        return ResponseEntity.ok(response);
    }
}
