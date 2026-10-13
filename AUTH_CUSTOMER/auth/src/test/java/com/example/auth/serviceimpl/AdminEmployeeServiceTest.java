package com.example.auth.serviceimpl;

import com.example.auth.dto.CreateEmployeeRequestDto;
import com.example.auth.dto.EmployeeResponseDto;
import com.example.auth.entity.AuthRole;
import com.example.auth.entity.AuthUsr;
import com.example.auth.repository.AuthRoleRepository;
import com.example.auth.repository.AuthUsrRepository;
import com.example.auth.security.JwtService;
import com.example.auth.service.LoginAuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class AdminEmployeeServiceTest {

    @Mock
    private AuthUsrRepository authUsrRepository;

    @Mock
    private AuthRoleRepository authRoleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private LoginAuditService loginAuditService;

    @InjectMocks
    private AuthServiceImpl authService;

    private AuthUsr headAdminUser;
    private AuthUsr adminUser;
    private AuthUsr staffUser;
    private AuthUsr customerUser;
    private AuthRole adminRole;
    private final String hashedPassword = BCrypt.hashpw("temp", BCrypt.gensalt());

    @BeforeEach
    void setUp() {
        AuthRole headAdminRole = new AuthRole();
        headAdminRole.setRoleId(1L);
        headAdminRole.setRoleCode("HEAD_ADMIN");

        AuthRole adminRole = new AuthRole();
        adminRole.setRoleId(2L);
        adminRole.setRoleCode("ADMIN");
        this.adminRole = adminRole;

        headAdminUser = new AuthUsr();
        headAdminUser.setUsername("headadmin");
        headAdminUser.setPasswordHash(hashedPassword);
        headAdminUser.setRole(headAdminRole);
        headAdminUser.setUserId(1L);

        adminUser = new AuthUsr();
        adminUser.setUsername("admin");
        adminUser.setPasswordHash(hashedPassword);
        adminUser.setRole(adminRole);
        adminUser.setUserId(2L);

        staffUser = new AuthUsr();
        staffUser.setUsername("staff");
        staffUser.setPasswordHash(hashedPassword);
        staffUser.setRole(adminRole);
        staffUser.setUserId(3L);

        customerUser = new AuthUsr();
        customerUser.setUsername("customer");
        customerUser.setPasswordHash(hashedPassword);
        customerUser.setRole(adminRole);
        customerUser.setUserId(4L);

        when(authRoleRepository.findByRoleCode(eq("HEAD_ADMIN"))).thenReturn(Optional.of(headAdminRole));
        when(authRoleRepository.findByRoleCode(eq("ADMIN"))).thenReturn(Optional.of(adminRole));
        when(passwordEncoder.encode(anyString())).thenReturn(hashedPassword);
    }

    @Test
    void testCreateEmployee_Success_HeadAdmin() {
        CreateEmployeeRequestDto request = new CreateEmployeeRequestDto();
        request.setUsername("jdoe");
        request.setEmail("jdoe@bank.local");
        request.setPhoneNumber("+1234567890");
        request.setPassword("P@ssw0rd!2025");

        when(authUsrRepository.findByUsername("headadmin")).thenReturn(Optional.of(headAdminUser));
        when(authUsrRepository.existsByUsername("jdoe")).thenReturn(false);
        when(authUsrRepository.existsByEmail("jdoe@bank.local")).thenReturn(false);
        when(authUsrRepository.existsByPhoneNumber("+1234567890")).thenReturn(false);

        AuthUsr created = new AuthUsr();
        created.setUserId(10L);
        created.setUsername("jdoe");
        created.setEmail("jdoe@bank.local");
        created.setPhoneNumber("+1234567890");
        created.setPasswordHash(hashedPassword);
        created.setRole(adminRole);
        created.setFailedLoginAttempts((short) 0);
        when(authUsrRepository.save(any(AuthUsr.class))).thenReturn(created);

        EmployeeResponseDto response = authService.createEmployee("headadmin", request);

        assertNotNull(response);
        assertEquals("jdoe", response.getUsername());
        assertEquals("jdoe@bank.local", response.getEmail());
        assertEquals("+1234567890", response.getPhoneNumber());
        assertEquals("ADMIN", response.getRole());
        assertEquals(Long.valueOf(10L), response.getUserId());

        // The request has no role field; the ADMIN role was chosen server-side.
        assertNotNull(request);

        // Verify password was hashed with BCrypt
        verify(passwordEncoder).encode("P@ssw0rd!2025");
    }

    @Test
    void testCreateEmployee_AdminCaller_Forbidden() {
        CreateEmployeeRequestDto request = new CreateEmployeeRequestDto();
        request.setUsername("jdoe");
        request.setPassword("P@ssw0rd!2025");

        when(authUsrRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));

        AccessDeniedException ex = assertThrows(
                AccessDeniedException.class,
                () -> authService.createEmployee("admin", request));

        assertEquals("Only HEAD_ADMIN users may create employees", ex.getMessage());
    }

    @Test
    void testCreateEmployee_StaffCaller_Forbidden() {
        CreateEmployeeRequestDto request = new CreateEmployeeRequestDto();
        request.setUsername("jdoe");
        request.setPassword("P@ssw0rd!2025");

        when(authUsrRepository.findByUsername("staff")).thenReturn(Optional.of(staffUser));

        assertThrows(AccessDeniedException.class,
                () -> authService.createEmployee("staff", request));
    }

    @Test
    void testCreateEmployee_CustomerCaller_Forbidden() {
        CreateEmployeeRequestDto request = new CreateEmployeeRequestDto();
        request.setUsername("jdoe");
        request.setPassword("P@ssw0rd!2025");

        when(authUsrRepository.findByUsername("customer")).thenReturn(Optional.of(customerUser));

        assertThrows(AccessDeniedException.class,
                () -> authService.createEmployee("customer", request));
    }

    @Test
    void testCreateEmployee_BlankUsername_Failure() {
        CreateEmployeeRequestDto request = new CreateEmployeeRequestDto();
        request.setUsername("");
        request.setPassword("P@ssw0rd!2025");

        when(authUsrRepository.findByUsername("headadmin")).thenReturn(Optional.of(headAdminUser));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.createEmployee("headadmin", request));
        assertEquals("Username is required", ex.getMessage());
    }

    @Test
    void testCreateEmployee_BlankPassword_Failure() {
        CreateEmployeeRequestDto request = new CreateEmployeeRequestDto();
        request.setUsername("jdoe");
        request.setPassword("");

        when(authUsrRepository.findByUsername("headadmin")).thenReturn(Optional.of(headAdminUser));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.createEmployee("headadmin", request));
        assertEquals("Password is required", ex.getMessage());
    }

    @Test
    void testCreateEmployee_DuplicateUsername_Failure() {
        CreateEmployeeRequestDto request = new CreateEmployeeRequestDto();
        request.setUsername("existing");
        request.setPassword("P@ssw0rd!2025");

        when(authUsrRepository.findByUsername("headadmin")).thenReturn(Optional.of(headAdminUser));
        when(authUsrRepository.existsByUsername("existing")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.createEmployee("headadmin", request));
        assertEquals("Username, email, or phone already exists", ex.getMessage());
    }

    @Test
    void testCreateEmployee_DuplicateEmail_Failure() {
        CreateEmployeeRequestDto request = new CreateEmployeeRequestDto();
        request.setUsername("newuser");
        request.setEmail("taken@bank.local");
        request.setPassword("P@ssw0rd!2025");

        when(authUsrRepository.findByUsername("headadmin")).thenReturn(Optional.of(headAdminUser));
        when(authUsrRepository.existsByUsername("newuser")).thenReturn(false);
        when(authUsrRepository.existsByEmail("taken@bank.local")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.createEmployee("headadmin", request));
        assertEquals("Username, email, or phone already exists", ex.getMessage());
    }

    @Test
    void testCreateEmployee_DuplicatePhone_Failure() {
        CreateEmployeeRequestDto request = new CreateEmployeeRequestDto();
        request.setUsername("newuser");
        request.setPhoneNumber("+9876543210");
        request.setPassword("P@ssw0rd!2025");

        when(authUsrRepository.findByUsername("headadmin")).thenReturn(Optional.of(headAdminUser));
        when(authUsrRepository.existsByUsername("newuser")).thenReturn(false);
        when(authUsrRepository.existsByPhoneNumber("+9876543210")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.createEmployee("headadmin", request));
        assertEquals("Username, email, or phone already exists", ex.getMessage());
    }

    @Test
    void testCreateEmployee_LoginAfterwards_Success() {
        // Simulate head admin creating an employee, then logging in as that employee
        CreateEmployeeRequestDto request = new CreateEmployeeRequestDto();
        request.setUsername("jdoe");
        request.setPassword("P@ssw0rd!2025");

        when(authUsrRepository.findByUsername("headadmin")).thenReturn(Optional.of(headAdminUser));
        when(authUsrRepository.existsByUsername("jdoe")).thenReturn(false);
        when(authUsrRepository.existsByEmail(anyString())).thenReturn(false);
        when(authUsrRepository.existsByPhoneNumber(anyString())).thenReturn(false);

        AuthUsr created = new AuthUsr();
        created.setUserId(10L);
        created.setUsername("jdoe");
        created.setPasswordHash(hashedPassword);
        created.setRole(adminRole);
        created.setFailedLoginAttempts((short) 0);
        when(authUsrRepository.save(any(AuthUsr.class))).thenReturn(created);
        when(authUsrRepository.findByUsername("jdoe")).thenReturn(Optional.of(created));

        EmployeeResponseDto response = authService.createEmployee("headadmin", request);
        assertNotNull(response);
        assertEquals("ADMIN", response.getRole());

        // Password verification (login) should succeed with BCrypt
        assertTrue(BCrypt.checkpw("P@ssw0rd!2025", hashedPassword));
    }
}
