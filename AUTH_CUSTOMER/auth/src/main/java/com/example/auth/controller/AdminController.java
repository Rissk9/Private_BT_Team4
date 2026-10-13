package com.example.auth.controller;

import com.example.auth.dto.CreateEmployeeRequestDto;
import com.example.auth.dto.EmployeeResponseDto;
import com.example.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Administrative APIs for managing staff. Requires JWT authentication with ADMIN role.")
public class AdminController {

    @Autowired
    private AuthService authService;

    @PostMapping("/employees")
    @Operation(summary = "Create a new staff member",
               description = "Creates a new staff member. Requires JWT authentication with ADMIN role. The created user will have STAFF role.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Staff member created successfully."),
        @ApiResponse(responseCode = "400", description = "Invalid request body."),
        @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid or missing JWT."),
        @ApiResponse(responseCode = "403", description = "Forbidden. Only ADMIN users can create staff.")
    })
    public ResponseEntity<EmployeeResponseDto> createEmployee(
            @RequestBody CreateEmployeeRequestDto request,
            Authentication authentication) {

        EmployeeResponseDto response = authService.createEmployee(
                authentication.getName(),
                request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}