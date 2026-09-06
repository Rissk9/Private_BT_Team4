package com.example.auth.serviceimpl;

import com.example.auth.dto.LoginRequestDto;
import com.example.auth.dto.LoginResponseDto;
import com.example.auth.dto.PendingRegistrationDto;
import com.example.auth.dto.RegisterRequestDto;
import com.example.auth.dto.ResetPasswordRequestDto;
import com.example.auth.entity.AuthRole;
import com.example.auth.entity.AuthUsr;
import com.example.auth.entity.CustDtl;
import com.example.auth.entity.CustStsMstr;
import com.example.auth.entity.CustTypMstr;
import com.example.auth.repository.AuthRoleRepository;
import com.example.auth.repository.AuthUsrRepository;
import com.example.auth.repository.CustDtlRepository;
import com.example.auth.repository.CustStsMstrRepository;
import com.example.auth.repository.CustTypMstrRepository;
import com.example.auth.security.JwtService;
import com.example.auth.service.AuthService;
import com.example.auth.service.OtpService;
import com.example.auth.util.AuditData;
import com.example.auth.util.AuditUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Identity;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthUsrRepository authUsrRepository;
    private final CustDtlRepository custDtlRepository;
    private final AuthRoleRepository authRoleRepository;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private AuditUtil auditUtil;

    @Autowired
    private CustTypMstrRepository custTypMstrRepository;
    
    @Autowired
    private CustStsMstrRepository custStsMstrRepository;

    private final Map<String, PendingRegistrationDto> pendingRegistrations =
            new ConcurrentHashMap<>();
    
    @Autowired
    JwtService jwtService;

    @Autowired
    public AuthServiceImpl(
            AuthUsrRepository authUsrRepository,
            CustDtlRepository custDtlRepository,
            AuthRoleRepository authRoleRepository,
            OtpService otpService,
            PasswordEncoder passwordEncoder) {

        this.authUsrRepository = authUsrRepository;
        this.custDtlRepository = custDtlRepository;
        this.authRoleRepository = authRoleRepository;
        this.otpService = otpService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(RegisterRequestDto request) {

        // Email OR phone is required
        if ((request.getEmail() == null || request.getEmail().isBlank())
                && (request.getPhoneNumber() == null
                || request.getPhoneNumber().isBlank())) {

            throw new IllegalArgumentException(
                    "Either email or phone number is required");
        }

        // Password is required
        if (request.getPassword() == null
                || request.getPassword().isBlank()) {

            throw new IllegalArgumentException(
                    "Password is required");
        }

        // Name is required
        if (request.getName() == null
                || request.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Name is required");
        }

        // Check existing email
        if (request.getEmail() != null
                && !request.getEmail().isBlank()
                && authUsrRepository.existsByEmail(request.getEmail())) {

            throw new IllegalArgumentException(
                    "Email is already registered");
        }

        // Check existing phone
        if (request.getPhoneNumber() != null
                && !request.getPhoneNumber().isBlank()
                && authUsrRepository.existsByPhoneNumber(
                        request.getPhoneNumber())) {

            throw new IllegalArgumentException(
                    "Phone number is already registered");
        }

        /*
         * Use email as identifier if available.
         * Otherwise use phone number.
         */
        String identifier = getIdentifier(request);

        // Temporarily store registration details
        PendingRegistrationDto pendingRegistration =
                new PendingRegistrationDto(
                        request.getName(),
                        request.getEmail(),
                        request.getPhoneNumber(),
                        request.getPassword()
                );

        pendingRegistrations.put(identifier, pendingRegistration);

        // Generate OTP
        otpService.generateAndSendOtp(identifier);
    }

    @Override
    @Transactional
    public boolean verifyRegistrationOtp(
            String identifier,
            String otp) {

        // Verify OTP first
        boolean verified = otpService.verifyOtp(identifier, otp);

        if (!verified) {
            return false;
        }

        // Get pending registration
        PendingRegistrationDto pendingRegistration =
                pendingRegistrations.get(identifier);

        if (pendingRegistration == null) {
            throw new IllegalArgumentException(
                    "Registration request not found or expired");
        }

        // Get CUSTOMER role
        AuthRole customerRole = authRoleRepository
                .findByRoleCode("CUSTOMER")
                .orElseThrow(() ->
                        new IllegalStateException(
                                "CUSTOMER role not found"));

        // Get INDIVIDUAL customer type
        CustTypMstr customerType = custTypMstrRepository
                .findByTypeCode("INDIVIDUAL")
                .orElseThrow(() ->
                        new IllegalStateException(
                                "INDIVIDUAL customer type not found"));

        // Get ACTIVE customer status
        CustStsMstr customerStatus = custStsMstrRepository
                .findByStatusCode("ACTIVE")
                .orElseThrow(() ->
                        new IllegalStateException(
                                "ACTIVE customer status not found"));

        /*
         * Create audit data
         */
        AuditData audit = auditUtil.createAudit("REGISTRATION");

        /*
         * Create customer
         */
        CustDtl customer = new CustDtl();

        customer.setFullName(
                pendingRegistration.getName());

        customer.setCustomerType(customerType);

        customer.setCustomerStatus(customerStatus);

        // Customer audit fields
        customer.setEffectiveDate(
                audit.getEffectiveDate());

        customer.setCrudValue(
                audit.getCrudValue());

        customer.setUserId(
                audit.getUserId());

        customer.setWorkstationId(
                audit.getWorkstationId());

        customer.setProgramId(
                audit.getProgramId());

        customer.setHostTimestamp(
                audit.getHostTimestamp());

        customer.setLocalTimestamp(
                audit.getLocalTimestamp());

        customer.setAcceptanceTimestamp(
                audit.getAcceptanceTimestamp());

        customer.setAcceptanceTimestampUtcOffset(
                audit.getAcceptanceTimestampUtcOffset());

        customer.setUuid(
                audit.getUuid());

        /*
         * Save customer.
         *
         * CSTID_IDFR is generated by the database.
         */
        CustDtl savedCustomer =
                custDtlRepository.save(customer);
        

        /*
         * Create authentication user
         */
        AuthUsr authUsr = new AuthUsr();

        // Link AUTH_USR to the newly created customer
        authUsr.setCustomer(savedCustomer);

        /*
         * USERNAME is mandatory.
         *
         * Use email when available,
         * otherwise use phone number.
         */
        String username;

        if (pendingRegistration.getEmail() != null
                && !pendingRegistration.getEmail().isBlank()) {

            username = pendingRegistration
                    .getEmail()
                    .trim()
                    .toLowerCase();

        } else {

            username = pendingRegistration
                    .getPhoneNumber()
                    .trim();
        }

        authUsr.setUsername(username);

        // Email can be null
        authUsr.setEmail(
                pendingRegistration.getEmail());

        // Phone can be null
        authUsr.setPhoneNumber(
                pendingRegistration.getPhoneNumber());

        // Hash password before storing
        authUsr.setPasswordHash(
                passwordEncoder.encode(
                        pendingRegistration.getPassword()));

        // Assign CUSTOMER role
        authUsr.setRole(customerRole);

        // Initial login state
        authUsr.setFailedLoginAttempts(
                (short) 0);

        /*
         * Apply audit data
         */
        authUsr.setEffectiveDate(
                audit.getEffectiveDate());

        authUsr.setCrudValue(
                audit.getCrudValue());

        authUsr.setAuditUserId(
                audit.getUserId());

        authUsr.setWorkstationId(
                audit.getWorkstationId());

        authUsr.setProgramId(
                audit.getProgramId());

        authUsr.setHostTimestamp(
                audit.getHostTimestamp());

        authUsr.setLocalTimestamp(
                audit.getLocalTimestamp());

        authUsr.setAcceptanceTimestamp(
                audit.getAcceptanceTimestamp());

        authUsr.setAcceptanceTimestampUtcOffset(
                audit.getAcceptanceTimestampUtcOffset());

        authUsr.setUuid(
                audit.getUuid());

        // Save authentication user
        authUsrRepository.save(authUsr);

        // Registration completed successfully
        pendingRegistrations.remove(identifier);

        return true;
    }

    private String getIdentifier(RegisterRequestDto request) {

        if (request.getEmail() != null
                && !request.getEmail().isBlank()) {

            return request.getEmail()
                    .trim()
                    .toLowerCase();
        }

        return request.getPhoneNumber()
                .trim();
    }
    
//    @Override
//    public String login(LoginRequestDto request) {
//
//        if (request.getUsername() == null
//                || request.getUsername().isBlank()) {
//
//            throw new IllegalArgumentException(
//                    "Username is required");
//        }
//
//        if (request.getPassword() == null
//                || request.getPassword().isBlank()) {
//
//            throw new IllegalArgumentException(
//                    "Password is required");
//        }
//
//        AuthUsr authUsr = authUsrRepository
//                .findByUsername(
//                        request.getUsername().trim())
//                .orElseThrow(() ->
//                        new IllegalArgumentException(
//                                "Invalid username or password"));
//
//        if (!passwordEncoder.matches(
//                request.getPassword(),
//                authUsr.getPasswordHash())) {
//
//            throw new IllegalArgumentException(
//                    "Invalid username or password");
//        }
//
//        return jwtService.generateToken(authUsr);
//    }
    
    @Override
    public LoginResponseDto login(LoginRequestDto request) {

        if (request.getUsername() == null
                || request.getUsername().isBlank()) {

            throw new IllegalArgumentException(
                    "Username is required");
        }

        if (request.getPassword() == null
                || request.getPassword().isBlank()) {

            throw new IllegalArgumentException(
                    "Password is required");
        }

        AuthUsr authUsr = authUsrRepository
                .findByUsername(
                        request.getUsername().trim())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid username or password"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                authUsr.getPasswordHash())) {

            throw new IllegalArgumentException(
                    "Invalid username or password");
        }

        String accessToken =
                jwtService.generateToken(authUsr);

        String refreshToken =
                jwtService.generateRefreshToken(authUsr);

        return new LoginResponseDto(
                accessToken,
                refreshToken
        );
    }
    @Override
    public LoginResponseDto refreshToken(String refreshToken) {

        if (refreshToken == null || refreshToken.isBlank()) {
            throw new IllegalArgumentException(
                    "Refresh token is required");
        }

        String username = jwtService.extractUsername(refreshToken);

        AuthUsr authUsr = authUsrRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid refresh token"));

        if (!jwtService.isTokenValid(refreshToken, username)) {
            throw new IllegalArgumentException(
                    "Invalid or expired refresh token");
        }

        String newAccessToken =
                jwtService.generateToken(authUsr);

        return new LoginResponseDto(
                newAccessToken,
                refreshToken
        );
    }
    
    @Override
    public void forgotPassword(String identifier) {
    	
    	if(identifier==null || identifier.isBlank())
    		throw new IllegalArgumentException("Email or Phone number is required");
    	
    	identifier=identifier.trim();
    	if(identifier.contains("@")) {
    		identifier=identifier.toLowerCase();
    		authUsrRepository.findByEmail(identifier).orElseThrow(()->new IllegalArgumentException("No account found with this email"));
    	}
    	
    	else {
			authUsrRepository.findByPhoneNumber(identifier).orElseThrow(()-> new IllegalArgumentException("No account found with this phone number"));
		}
    	
    	otpService.generateAndSendOtp(identifier);
    }
    
    @Override
    @Transactional
    public void resetPassword(@RequestBody ResetPasswordRequestDto request) {
    	
    	String identifier=request.getIdentifier().trim();
    	
    	boolean verified=otpService.verifyOtp(identifier, request.getOtp());
    	
    	if(!verified)
    		throw new IllegalArgumentException("Invalid or expired OTP");
    	
    	if(!request.getNewPassword().equals(request.getConfirmPassword()))
    		throw new IllegalArgumentException("New password not matching confirmation password");
 
    	AuthUsr authUsr;
    	if(request.getIdentifier().contains("@")) {
    		authUsr=authUsrRepository.findByEmail(identifier).orElseThrow(()->new RuntimeException("User not present with this email"));
    	}
    	else {
    		authUsr=authUsrRepository.findByPhoneNumber(identifier).orElseThrow(()->new RuntimeException("User not present with this phone number"));
		}
    	
    	authUsr.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
    	
    	authUsrRepository.save(authUsr);
    }
}