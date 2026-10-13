package com.example.auth.serviceimpl;

import com.example.auth.dto.CreateEmployeeRequestDto;
import com.example.auth.dto.EmployeeResponseDto;
import com.example.auth.dto.LoginRequestDto;
import com.example.auth.dto.LoginResponseDto;
import com.example.auth.dto.PendingRegistrationDto;
import com.example.auth.dto.RegisterRequestDto;
import com.example.auth.dto.ResetPasswordRequestDto;
import com.example.auth.dto.UserResponseDto;
import com.example.auth.dto.UserUpdateRequestDto;
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

        @Autowired
        private com.example.auth.repository.CustNmeCmpntRepository custNmeCmpntRepository;

        @Autowired
        private com.example.auth.service.LoginAuditService loginAuditService;

        @Autowired
        private com.example.auth.repository.CustIdfnRepository custIdfnRepository;

        @Autowired
        private com.example.auth.repository.CustAddrCmpntRepository custAddrCmpntRepository;

        @Autowired
        private com.example.auth.repository.CustCntctDtlsRepository custCntctDtlsRepository;

        private final Map<String, PendingRegistrationDto> pendingRegistrations = new ConcurrentHashMap<>();

        @Autowired
        JwtService jwtService;

        @Autowired
        public AuthServiceImpl(
                        AuthUsrRepository authUsrRepository,
                        CustDtlRepository custDtlRepository,
                        AuthRoleRepository authRoleRepository,
                        OtpService otpService,
                        PasswordEncoder passwordEncoder,
                        com.example.auth.service.LoginAuditService loginAuditService,
                        JwtService jwtService) {

                this.authUsrRepository = authUsrRepository;
                this.custDtlRepository = custDtlRepository;
                this.authRoleRepository = authRoleRepository;
                this.otpService = otpService;
                this.passwordEncoder = passwordEncoder;
                this.loginAuditService = loginAuditService;
                this.jwtService = jwtService;
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
                PendingRegistrationDto pendingRegistration = new PendingRegistrationDto(
                                request.getName(),
                                request.getEmail(),
                                request.getPhoneNumber(),
                                request.getPassword());

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
                PendingRegistrationDto pendingRegistration = pendingRegistrations.get(identifier);

                if (pendingRegistration == null) {
                        throw new IllegalArgumentException(
                                        "Registration request not found or expired");
                }

                // Get CUSTOMER role
                AuthRole customerRole = authRoleRepository
                                .findByRoleCode("CUSTOMER")
                                .orElseThrow(() -> new IllegalStateException(
                                                "CUSTOMER role not found"));

                // Get INDIVIDUAL customer type
                CustTypMstr customerType = custTypMstrRepository
                                .findByTypeCode("INDIVIDUAL")
                                .orElseThrow(() -> new IllegalStateException(
                                                "INDIVIDUAL customer type not found"));

                // Get KYC PENDING customer status
                CustStsMstr customerStatus = custStsMstrRepository
                                .findByStatusCode("KYC_PENDING")
                                .orElseThrow(() -> new IllegalStateException(
                                                "KYC_PENDING customer status not found"));

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
                CustDtl savedCustomer = custDtlRepository.save(customer);

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

        // @Override
        // public String login(LoginRequestDto request) {
        //
        // if (request.getUsername() == null
        // || request.getUsername().isBlank()) {
        //
        // throw new IllegalArgumentException(
        // "Username is required");
        // }
        //
        // if (request.getPassword() == null
        // || request.getPassword().isBlank()) {
        //
        // throw new IllegalArgumentException(
        // "Password is required");
        // }
        //
        // AuthUsr authUsr = authUsrRepository
        // .findByUsername(
        // request.getUsername().trim())
        // .orElseThrow(() ->
        // new IllegalArgumentException(
        // "Invalid username or password"));
        //
        // if (!passwordEncoder.matches(
        // request.getPassword(),
        // authUsr.getPasswordHash())) {
        //
        // throw new IllegalArgumentException(
        // "Invalid username or password");
        // }
        //
        // return jwtService.generateToken(authUsr);
        // }

        @Override
        public LoginResponseDto login(LoginRequestDto request, String ipAddress, String userAgent) {

                if (request.getUsername() == null
                                || request.getUsername().isBlank()) {
                        throw new IllegalArgumentException("Username is required");
                }

                if (request.getPassword() == null
                                || request.getPassword().isBlank()) {
                        throw new IllegalArgumentException("Password is required");
                }

                AuthUsr authUsr = authUsrRepository
                                .findByUsername(request.getUsername().trim())
                                .orElse(null);

                if (authUsr == null) {
                        loginAuditService.recordAudit(null, "FAILED", "UNKNOWN_USERNAME", ipAddress, userAgent);
                        throw new IllegalArgumentException("Invalid username or password");
                }

                if (!passwordEncoder.matches(request.getPassword(), authUsr.getPasswordHash())) {
                        loginAuditService.recordAudit(authUsr, "FAILED", "INCORRECT_PASSWORD", ipAddress, userAgent);
                        throw new IllegalArgumentException("Invalid username or password");
                }

                loginAuditService.recordAudit(authUsr, "SUCCESS", null, ipAddress, userAgent);

                String accessToken = jwtService.generateToken(authUsr);
                String refreshToken = jwtService.generateRefreshToken(authUsr);

                return new LoginResponseDto(accessToken, refreshToken);
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
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Invalid refresh token"));

                if (!jwtService.isTokenValid(refreshToken, username)) {
                        throw new IllegalArgumentException(
                                        "Invalid or expired refresh token");
                }

                String newAccessToken = jwtService.generateToken(authUsr);

                return new LoginResponseDto(
                                newAccessToken,
                                refreshToken);
        }

        @Override
        public void forgotPassword(String identifier) {

                if (identifier == null || identifier.isBlank())
                        throw new IllegalArgumentException("Email or Phone number is required");

                identifier = identifier.trim();
                if (identifier.contains("@")) {
                        identifier = identifier.toLowerCase();
                        authUsrRepository.findByEmail(identifier).orElseThrow(
                                        () -> new IllegalArgumentException("No account found with this email"));
                }

                else {
                        authUsrRepository.findByPhoneNumber(identifier).orElseThrow(
                                        () -> new IllegalArgumentException("No account found with this phone number"));
                }

                otpService.generateAndSendOtp(identifier);
        }

        @Override
        @Transactional
        public void resetPassword(@RequestBody ResetPasswordRequestDto request) {

                String identifier = request.getIdentifier().trim();

                boolean verified = otpService.verifyOtp(identifier, request.getOtp());

                if (!verified)
                        throw new IllegalArgumentException("Invalid or expired OTP");

                if (!request.getNewPassword().equals(request.getConfirmPassword()))
                        throw new IllegalArgumentException("New password not matching confirmation password");

                AuthUsr authUsr;
                if (request.getIdentifier().contains("@")) {
                        authUsr = authUsrRepository.findByEmail(identifier)
                                        .orElseThrow(() -> new RuntimeException("User not present with this email"));
                } else {
                        authUsr = authUsrRepository.findByPhoneNumber(identifier).orElseThrow(
                                        () -> new RuntimeException("User not present with this phone number"));
                }

                authUsr.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));

                authUsrRepository.save(authUsr);
        }

        @Override
        public void logout(String token, String username) {
                if (token == null || token.isBlank()) {
                        return;
                }

                jwtService.invalidateToken(token);

                AuthUsr authUsr = authUsrRepository.findByUsername(username).orElse(null);
                if (authUsr != null) {
                        loginAuditService.updateLogoutTimestamp(authUsr);
                }
        }

        @Override
        @Transactional(readOnly = true)
        public UserResponseDto getUserProfile(String username) {
                AuthUsr authUsr = authUsrRepository.findByUsername(username)
                                .orElseThrow(() -> new IllegalArgumentException("User not found"));

                UserResponseDto dto = new UserResponseDto();
                dto.setUsername(authUsr.getUsername());
                dto.setEmail(authUsr.getEmail());
                dto.setPhoneNumber(authUsr.getPhoneNumber());
                if (authUsr.getRole() != null) {
                        dto.setRole(authUsr.getRole().getRoleCode());
                }

                CustDtl customer = authUsr.getCustomer();
                if (customer != null) {
                        dto.setCustomerId(customer.getCustomerIdentifier());
                        dto.setDateOfBirth(customer.getDateOfBirth());
                        dto.setGender(customer.getGender());
                        dto.setPreferredLanguage(customer.getPreferredLanguage());
                        dto.setCountryOfOrigin(customer.getCountryOfOrigin());
                        if (customer.getCustomerStatus() != null) {
                                dto.setCustomerStatus(customer.getCustomerStatus().getStatusCode());
                        }

                        // Name
                        java.util.List<com.example.auth.entity.CustNmeCmpnt> names = custNmeCmpntRepository
                                        .findByCustomer(customer);
                        for (com.example.auth.entity.CustNmeCmpnt n : names) {
                                String type = n.getNameType().getNameType();
                                if ("FIRST_NAME".equals(type))
                                        dto.setFirstName(n.getNameTypeValue());
                                else if ("MIDDLE_NAME".equals(type))
                                        dto.setMiddleName(n.getNameTypeValue());
                                else if ("LAST_NAME".equals(type))
                                        dto.setLastName(n.getNameTypeValue());
                        }

                        // Identification
                        java.util.List<com.example.auth.entity.CustIdfn> idfns = custIdfnRepository
                                        .findByCustomer(customer);
                        if (!idfns.isEmpty()) {
                                com.example.auth.entity.CustIdfn idfn = idfns.get(0); // Take the first one
                                dto.setIdentificationType(idfn.getIdentificationType().getIdentificationType());
                                String idNum = idfn.getIdentificationItem();
                                dto.setIdentificationNumber(
                                                com.example.auth.util.MaskingUtil.maskIdentificationNumber(idNum));
                        }

                        // Address
                        java.util.List<com.example.auth.entity.CustAddrCmpnt> addrs = custAddrCmpntRepository
                                        .findByCustomer(customer);
                        for (com.example.auth.entity.CustAddrCmpnt a : addrs) {
                                String type = a.getAddressType().getAddressType();
                                if ("ADDRESS_LINE_1".equals(type))
                                        dto.setAddressLine1(a.getAddressTypeValue());
                                else if ("ADDRESS_LINE_2".equals(type))
                                        dto.setAddressLine2(a.getAddressTypeValue());
                                else if ("CITY".equals(type))
                                        dto.setCity(a.getAddressTypeValue());
                                else if ("STATE".equals(type))
                                        dto.setState(a.getAddressTypeValue());
                                else if ("COUNTRY".equals(type))
                                        dto.setCountry(a.getAddressTypeValue());
                                else if ("POSTAL_CODE".equals(type))
                                        dto.setPostalCode(a.getAddressTypeValue());
                        }

                        // Contact Information (masked)
                        // Use AuthUsr as the source of truth for email and phone since PUT /users/me
                        // updates AuthUsr
                        dto.setMaskedEmail(com.example.auth.util.MaskingUtil.maskEmail(authUsr.getEmail()));
                        dto.setMaskedMobileNumber(
                                        com.example.auth.util.MaskingUtil.maskMobileNumber(authUsr.getPhoneNumber()));

                        java.util.List<com.example.auth.entity.CustCntctDtls> contacts = custCntctDtlsRepository
                                        .findByCustomer(customer);
                        for (com.example.auth.entity.CustCntctDtls c : contacts) {
                                String type = c.getContactType().getContactType();
                                String val = c.getContactValue();
                                if ("MOBILE".equals(type) && val != null && (authUsr.getPhoneNumber() == null
                                                || authUsr.getPhoneNumber().isBlank())) {
                                        dto.setPhoneNumber(val);
                                        dto.setMaskedMobileNumber(
                                                        com.example.auth.util.MaskingUtil.maskMobileNumber(val));
                                } else if ("EMAIL".equals(type) && val != null
                                                && (authUsr.getEmail() == null || authUsr.getEmail().isBlank())) {
                                        dto.setEmail(val);
                                        dto.setMaskedEmail(com.example.auth.util.MaskingUtil.maskEmail(val));
                                }
                        }
                }

                return dto;
        }

        @Override
        @Transactional
        public UserResponseDto updateUserProfile(String username, UserUpdateRequestDto request) {
                AuthUsr authUsr = authUsrRepository.findByUsername(username)
                                .orElseThrow(() -> new IllegalArgumentException("User not found"));

                // Update email if provided and not blank, and check uniqueness if changed
                if (request.getEmail() != null && !request.getEmail().isBlank()) {
                        String newEmail = request.getEmail().trim().toLowerCase();
                        if (!newEmail.equals(authUsr.getEmail())) {
                                if (authUsrRepository.existsByEmail(newEmail)) {
                                        throw new IllegalArgumentException(
                                                        "Email is already registered by another user");
                                }
                                authUsr.setEmail(newEmail);
                        }
                }

                // Update phone if provided and not blank, and check uniqueness if changed
                if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
                        String newPhone = request.getPhoneNumber().trim();
                        if (!newPhone.equals(authUsr.getPhoneNumber())) {
                                if (authUsrRepository.existsByPhoneNumber(newPhone)) {
                                        throw new IllegalArgumentException(
                                                        "Phone number is already registered by another user");
                                }
                                authUsr.setPhoneNumber(newPhone);
                        }
                }

                authUsrRepository.save(authUsr);
                return getUserProfile(username);
        }

        /**
         * Creates a new employee account. Only a logged-in HEAD_ADMIN may call this.
         * The role is assigned server-side as ADMIN - callers cannot supply a role.
         */
        @Override
        @Transactional
        public EmployeeResponseDto createEmployee(String callerUsername,
                CreateEmployeeRequestDto request) {

                // Authorization is enforced by this method: the caller's role in the DB
                // must be HEAD_ADMIN. The client cannot influence the assigned role.
                AuthUsr caller = authUsrRepository.findByUsername(callerUsername)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Authenticated caller not found"));

                AuthRole adminRole = authRoleRepository.findByRoleCode("ADMIN")
                                .orElseThrow(() -> new IllegalStateException(
                                                "ADMIN role not configured"));

                // Only ADMIN may create employees; assigned role is STAFF
                if (caller.getRole() == null
                                || !caller.getRole().getRoleCode().equals("ADMIN")
                                || !caller.getRole().getRoleId().equals(adminRole.getRoleId())) {

                        throw new org.springframework.security.access.AccessDeniedException(
                                        "Only ADMIN users may create staff");
                }

                if (request.getUsername() == null
                                || request.getUsername().isBlank()) {

                        throw new IllegalArgumentException("Username is required");
                }

                if (request.getPassword() == null
                                || request.getPassword().isBlank()) {

                        throw new IllegalArgumentException("Password is required");
                }

                String username = request.getUsername().trim().toLowerCase();

                if (existsUsernameOrEmailOrPhone(username, request.getEmail(),
                                request.getPhoneNumber())) {

                        throw new IllegalArgumentException("Username, email, or phone already exists");
                }

                AuthRole staffRole = authRoleRepository.findByRoleCode("STAFF")
                                .orElseThrow(() -> new IllegalStateException(
                                                "STAFF role not found"));

                AuditData audit = auditUtil.createAudit("ADMIN_CREATE_EMPLOYEE");

                AuthUsr employee = new AuthUsr();
                employee.setUsername(username);
                if (request.getEmail() != null && !request.getEmail().isBlank()) {
                        employee.setEmail(request.getEmail().trim().toLowerCase());
                }
                if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
                        employee.setPhoneNumber(request.getPhoneNumber().trim());
                }
                employee.setPasswordHash(passwordEncoder.encode(request.getPassword()));
                employee.setRole(staffRole);
                employee.setFailedLoginAttempts((short) 0);

                employee.setEffectiveDate(audit.getEffectiveDate());
                employee.setCrudValue(audit.getCrudValue());
                employee.setAuditUserId(audit.getUserId());
                employee.setWorkstationId(audit.getWorkstationId());
                employee.setProgramId(audit.getProgramId());
                employee.setHostTimestamp(audit.getHostTimestamp());
                employee.setLocalTimestamp(audit.getLocalTimestamp());
                employee.setAcceptanceTimestamp(audit.getAcceptanceTimestamp());
                employee.setAcceptanceTimestampUtcOffset(audit.getAcceptanceTimestampUtcOffset());
                employee.setUuid(audit.getUuid());

                employee = authUsrRepository.save(employee);

                EmployeeResponseDto response = new EmployeeResponseDto();
                response.setUserId(employee.getUserId());
                response.setUsername(employee.getUsername());
                response.setEmail(employee.getEmail());
                response.setPhoneNumber(employee.getPhoneNumber());
                response.setRole(employee.getRole().getRoleCode());

                return response;
        }

        private boolean existsUsernameOrEmailOrPhone(String username, String email,
                String phoneNumber) {

                if (authUsrRepository.existsByUsername(username)
                                || authUsrRepository.existsByEmail(email)
                                || authUsrRepository.existsByPhoneNumber(phoneNumber)) {

                        return true;
                }

                if (authUsrRepository.findByUsername(username).map(u -> {
                        boolean matchEmail = (email != null && !email.isBlank())
                                        && email.equalsIgnoreCase(u.getEmail());
                        boolean matchPhone = (phoneNumber != null && !phoneNumber.isBlank())
                                        && phoneNumber.trim().equalsIgnoreCase(u.getPhoneNumber());
                        return matchEmail || matchPhone;
                }).orElse(false)) {

                        return true;
                }

                return false;
        }
}