package com.example.auth.controller;

import com.example.auth.dto.CustomerProfileRequestDto;
import com.example.auth.dto.CustomerProfileResponseDto;
import com.example.auth.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/customer")
@Tag(name = "Customer Management", description = "Customer profile, KYC, and account management APIs. Requires JWT authentication.")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/profile")
    @Operation(summary = "Submit customer KYC profile",
               description = "Submits or updates the KYC profile for the currently authenticated customer.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "KYC profile submitted/updated successfully."),
        @ApiResponse(responseCode = "400", description = "Invalid request body."),
        @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid or missing JWT.")
    })
    public ResponseEntity<CustomerProfileResponseDto> submitProfile(
            @RequestBody CustomerProfileRequestDto request,
            Authentication authentication) {

        String username = authentication.getName();
        CustomerProfileResponseDto response = customerService.submitKycProfile(request, username);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    @Operation(summary = "Get customer KYC profile",
               description = "Retrieves the KYC profile of the currently authenticated customer.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "KYC profile retrieved successfully."),
        @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid or missing JWT.")
    })
    public ResponseEntity<CustomerProfileResponseDto> getProfile(Authentication authentication) {
        String username = authentication.getName();
        CustomerProfileResponseDto response = customerService.getCustomerProfile(username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    @Operation(summary = "Get customer status",
               description = "Retrieves the account status of the currently authenticated customer.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Customer status retrieved successfully."),
        @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid or missing JWT.")
    })
    public ResponseEntity<java.util.Map<String, String>> getCustomerStatus(Authentication authentication) {
        String username = authentication.getName();
        String status = customerService.getCustomerStatus(username);
        return ResponseEntity.ok(java.util.Map.of("status", status));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all customers",
               description = "Retrieves a list of all customers (requires admin privileges).",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of customers retrieved successfully."),
        @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid or missing JWT.")
    })
    public ResponseEntity<java.util.List<CustomerProfileResponseDto>> getAllCustomers(Authentication authentication) {
        String username = authentication.getName();
        java.util.List<CustomerProfileResponseDto> response = customerService.getAllCustomers(username);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/account")
    @Operation(summary = "Open a new customer account",
               description = "Opens a new bank account for the currently authenticated customer.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Account opened successfully."),
        @ApiResponse(responseCode = "400", description = "Invalid request body."),
        @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid or missing JWT.")
    })
    public ResponseEntity<com.example.auth.dto.OpenAccountResponseDto> openAccount(
            @RequestBody com.example.auth.dto.OpenAccountRequestDto request,
            Authentication authentication) {

        String username = authentication.getName();
        com.example.auth.dto.OpenAccountResponseDto response = customerService.openAccount(request, username);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/account")
    @Operation(summary = "Get customer accounts",
               description = "Retrieves a list of all accounts of the currently authenticated customer.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of accounts retrieved successfully."),
        @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid or missing JWT.")
    })
    public ResponseEntity<java.util.List<com.example.auth.dto.OpenAccountResponseDto>> getAccounts(Authentication authentication) {
        String username = authentication.getName();
        java.util.List<com.example.auth.dto.OpenAccountResponseDto> response = customerService.getAccounts(username);
        return ResponseEntity.ok(response);
    }
}
