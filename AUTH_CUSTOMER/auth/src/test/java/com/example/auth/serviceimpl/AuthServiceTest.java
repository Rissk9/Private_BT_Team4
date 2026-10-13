package com.example.auth.serviceimpl;

import com.example.auth.dto.LoginRequestDto;
import com.example.auth.entity.AuthUsr;
import com.example.auth.repository.AuthUsrRepository;
import com.example.auth.security.JwtService;
import com.example.auth.service.LoginAuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private AuthUsrRepository authUsrRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private LoginAuditService loginAuditService;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Success() {
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("testuser");
        request.setPassword("password123");

        AuthUsr mockUser = new AuthUsr();
        mockUser.setUsername("testuser");
        mockUser.setPasswordHash("hashedpassword");

        when(authUsrRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("password123", "hashedpassword")).thenReturn(true);
        when(jwtService.generateToken(mockUser)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(mockUser)).thenReturn("refresh-token");

        var response = authService.login(request, "127.0.0.1", "TestAgent");

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        
        // Verify audit log
        verify(loginAuditService).recordAudit(mockUser, "SUCCESS", null, "127.0.0.1", "TestAgent");
    }

    @Test
    void testLogin_UnknownUsername() {
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("unknownuser");
        request.setPassword("password123");

        when(authUsrRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(request, "127.0.0.1", "TestAgent");
        });

        assertEquals("Invalid username or password", exception.getMessage());

        // Verify audit log
        verify(loginAuditService).recordAudit(null, "FAILED", "UNKNOWN_USERNAME", "127.0.0.1", "TestAgent");
    }

    @Test
    void testLogin_WrongPassword() {
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("testuser");
        request.setPassword("wrongpassword");

        AuthUsr mockUser = new AuthUsr();
        mockUser.setUsername("testuser");
        mockUser.setPasswordHash("hashedpassword");

        when(authUsrRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("wrongpassword", "hashedpassword")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(request, "127.0.0.1", "TestAgent");
        });

        assertEquals("Invalid username or password", exception.getMessage());

        // Verify audit log
        verify(loginAuditService).recordAudit(mockUser, "FAILED", "INCORRECT_PASSWORD", "127.0.0.1", "TestAgent");
    }

    @Test
    void testLogout() {
        AuthUsr mockUser = new AuthUsr();
        mockUser.setUsername("testuser");

        when(authUsrRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));

        authService.logout("mock-token", "testuser");

        verify(jwtService).invalidateToken("mock-token");
        verify(loginAuditService).updateLogoutTimestamp(mockUser);
    }
}
