package com.example.auth.controller;

import com.example.auth.dto.UserResponseDto;
import com.example.auth.dto.UserUpdateRequestDto;
import com.example.auth.service.AuthService;
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
@RequestMapping("/users")
@Tag(name = "User Management", description = "User profile and account management APIs. Requires JWT authentication.")
public class UserController {

    @Autowired
    private AuthService authService;

    @GetMapping("/me")
    @Operation(summary = "Get current user profile",
               description = "Retrieves the profile of the currently authenticated user.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User profile retrieved successfully."),
        @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid or missing JWT.")
    })
    public ResponseEntity<UserResponseDto> getMe(Authentication authentication) {
        String username = authentication.getName();
        UserResponseDto response = authService.getUserProfile(username);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user profile",
               description = "Updates the profile of the currently authenticated user.",
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User profile updated successfully."),
        @ApiResponse(responseCode = "400", description = "Invalid request body."),
        @ApiResponse(responseCode = "401", description = "Unauthorized. Invalid or missing JWT.")
    })
    public ResponseEntity<UserResponseDto> updateMe(
            @RequestBody UserUpdateRequestDto request,
            Authentication authentication) {
        String username = authentication.getName();
        UserResponseDto response = authService.updateUserProfile(username, request);
        return ResponseEntity.ok(response);
    }
}
