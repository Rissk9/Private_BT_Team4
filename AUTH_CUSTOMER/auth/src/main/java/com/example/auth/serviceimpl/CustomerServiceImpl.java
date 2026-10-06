package com.example.auth.serviceimpl;

import com.example.auth.dto.CustomerProfileRequestDto;
import com.example.auth.dto.CustomerProfileResponseDto;
import com.example.auth.entity.*;
import com.example.auth.repository.*;
import com.example.auth.service.CustomerService;
import com.example.auth.util.AuditData;
import com.example.auth.util.AuditUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private AuthUsrRepository authUsrRepository;

    @Autowired
    private CustDtlRepository custDtlRepository;

    @Autowired
    private CustStsMstrRepository custStsMstrRepository;

    @Autowired
    private CustIdfnRepository custIdfnRepository;

    @Autowired
    private CustAddrCmpntRepository custAddrCmpntRepository;

    @Autowired
    private CustCntctDtlsRepository custCntctDtlsRepository;

    @Autowired
    private CustNmeCmpntRepository custNmeCmpntRepository;

    @Autowired
    private IdfnTypMstrRepository idfnTypMstrRepository;

    @Autowired
    private AddrTypMstrRepository addrTypMstrRepository;

    @Autowired
    private CntctTypMstrRepository cntctTypMstrRepository;

    @Autowired
    private NmeTypMstrRepository nmeTypMstrRepository;

    @Autowired
    private AuditUtil auditUtil;

    @Override
    @Transactional
    public CustomerProfileResponseDto submitKycProfile(CustomerProfileRequestDto request, String username) {
        
        AuthUsr authUsr = authUsrRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        CustDtl customer = authUsr.getCustomer();
        if (customer == null) {
            throw new IllegalStateException("No customer associated with this user");
        }

        if (!"KYC_PENDING".equals(customer.getCustomerStatus().getStatusCode())) {
            throw new IllegalStateException("Customer status must be KYC_PENDING to perform KYC");
        }

        // 1. Update CustDtl
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setGender(request.getGender());
        customer.setPreferredLanguage(request.getPreferredLanguage());
        customer.setCountryOfOrigin(request.getCountryOfOrigin());
        
        String fullName = buildFullName(request.getFirstName(), request.getMiddleName(), request.getLastName());
        customer.setFullName(fullName);
        
        AuditData audit = auditUtil.createAudit("CUSTOMER_KYC");

        customer.setEffectiveDate(audit.getEffectiveDate());
        customer.setCrudValue("U");
        customer.setUserId(audit.getUserId());
        customer.setWorkstationId(audit.getWorkstationId());
        customer.setProgramId(audit.getProgramId());
        customer.setHostTimestamp(audit.getHostTimestamp());
        customer.setLocalTimestamp(audit.getLocalTimestamp());
        customer.setAcceptanceTimestamp(audit.getAcceptanceTimestamp());
        customer.setAcceptanceTimestampUtcOffset(audit.getAcceptanceTimestampUtcOffset());
        customer.setUuid(audit.getUuid());

        // 2. Save Name Components
        saveNameComponent(customer, "FIRST_NAME", request.getFirstName(), audit);
        if (request.getMiddleName() != null && !request.getMiddleName().isBlank()) {
            saveNameComponent(customer, "MIDDLE_NAME", request.getMiddleName(), audit);
        }
        saveNameComponent(customer, "LAST_NAME", request.getLastName(), audit);

        // 3. Save Identification
        if (request.getIdentificationType() != null && request.getIdentificationNumber() != null) {
            IdfnTypMstr idfnType = idfnTypMstrRepository.findByIdentificationType(request.getIdentificationType())
                    .orElseThrow(() -> new IllegalStateException("Invalid identification type: " + request.getIdentificationType()));
            
            CustIdfn custIdfn = new CustIdfn();
            custIdfn.setCustomer(customer);
            custIdfn.setIdentificationType(idfnType);
            custIdfn.setIdentificationItem(request.getIdentificationNumber());
            
            custIdfn.setEffectiveDate(audit.getEffectiveDate());
            custIdfn.setCrudValue(audit.getCrudValue());
            custIdfn.setUserId(audit.getUserId());
            custIdfn.setWorkstationId(audit.getWorkstationId());
            custIdfn.setProgramId(audit.getProgramId());
            custIdfn.setHostTimestamp(audit.getHostTimestamp());
            custIdfn.setLocalTimestamp(audit.getLocalTimestamp());
            custIdfn.setAcceptanceTimestamp(audit.getAcceptanceTimestamp());
            custIdfn.setAcceptanceTimestampUtcOffset(audit.getAcceptanceTimestampUtcOffset());
            custIdfn.setUuid(audit.getUuid());
            
            custIdfnRepository.save(custIdfn);
        }

        // 4. Save Address Components
        saveAddressComponent(customer, "ADDRESS_LINE_1", request.getAddressLine1(), audit);
        if (request.getAddressLine2() != null && !request.getAddressLine2().isBlank()) {
            saveAddressComponent(customer, "ADDRESS_LINE_2", request.getAddressLine2(), audit);
        }
        saveAddressComponent(customer, "CITY", request.getCity(), audit);
        saveAddressComponent(customer, "STATE", request.getState(), audit);
        saveAddressComponent(customer, "COUNTRY", request.getCountry(), audit);
        saveAddressComponent(customer, "POSTAL_CODE", request.getPostalCode(), audit);

        // 5. Save Contact Components
        if (request.getMobileNumber() != null && !request.getMobileNumber().isBlank()) {
            saveContactComponent(customer, "MOBILE", request.getMobileNumber(), audit);
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            saveContactComponent(customer, "EMAIL", request.getEmail(), audit);
        }

        // 6. Transition Status to ACTIVE
        CustStsMstr activeStatus = custStsMstrRepository.findByStatusCode("ACTIVE")
                .orElseThrow(() -> new IllegalStateException("ACTIVE customer status not found"));
        customer.setCustomerStatus(activeStatus);

        custDtlRepository.save(customer);

        // 7. Return Response
        CustomerProfileResponseDto response = new CustomerProfileResponseDto();
        response.setMessage("Customer KYC completed successfully");
        response.setStatus("ACTIVE");
        response.setCustomerId(customer.getCustomerIdentifier());
        response.setFullName(customer.getFullName());

        return response;
    }

    private void saveNameComponent(CustDtl customer, String nameTypeStr, String value, AuditData audit) {
        if (value == null || value.isBlank()) return;
        NmeTypMstr nameType = nmeTypMstrRepository.findByNameType(nameTypeStr)
                .orElseThrow(() -> new IllegalStateException("Invalid name type: " + nameTypeStr));
        CustNmeCmpnt cmpnt = new CustNmeCmpnt();
        cmpnt.setCustomer(customer);
        cmpnt.setNameType(nameType);
        cmpnt.setNameTypeValue(value);
        cmpnt.setEffectiveDate(audit.getEffectiveDate());
        cmpnt.setCrudValue(audit.getCrudValue());
        cmpnt.setUserId(audit.getUserId());
        cmpnt.setWorkstationId(audit.getWorkstationId());
        cmpnt.setProgramId(audit.getProgramId());
        cmpnt.setHostTimestamp(audit.getHostTimestamp());
        cmpnt.setLocalTimestamp(audit.getLocalTimestamp());
        cmpnt.setAcceptanceTimestamp(audit.getAcceptanceTimestamp());
        cmpnt.setAcceptanceTimestampUtcOffset(audit.getAcceptanceTimestampUtcOffset());
        cmpnt.setUuid(audit.getUuid());
        custNmeCmpntRepository.save(cmpnt);
    }

    private void saveAddressComponent(CustDtl customer, String addrTypeStr, String value, AuditData audit) {
        if (value == null || value.isBlank()) return;
        AddrTypMstr addrType = addrTypMstrRepository.findByAddressType(addrTypeStr)
                .orElseThrow(() -> new IllegalStateException("Invalid address type: " + addrTypeStr));
        CustAddrCmpnt cmpnt = new CustAddrCmpnt();
        cmpnt.setCustomer(customer);
        cmpnt.setAddressType(addrType);
        cmpnt.setAddressTypeValue(value);
        cmpnt.setEffectiveDate(audit.getEffectiveDate());
        cmpnt.setCrudValue(audit.getCrudValue());
        cmpnt.setUserId(audit.getUserId());
        cmpnt.setWorkstationId(audit.getWorkstationId());
        cmpnt.setProgramId(audit.getProgramId());
        cmpnt.setHostTimestamp(audit.getHostTimestamp());
        cmpnt.setLocalTimestamp(audit.getLocalTimestamp());
        cmpnt.setAcceptanceTimestamp(audit.getAcceptanceTimestamp());
        cmpnt.setAcceptanceTimestampUtcOffset(audit.getAcceptanceTimestampUtcOffset());
        cmpnt.setUuid(audit.getUuid());
        custAddrCmpntRepository.save(cmpnt);
    }

    private void saveContactComponent(CustDtl customer, String contactTypeStr, String value, AuditData audit) {
        if (value == null || value.isBlank()) return;
        CntctTypMstr cntctType = cntctTypMstrRepository.findByContactType(contactTypeStr)
                .orElseThrow(() -> new IllegalStateException("Invalid contact type: " + contactTypeStr));
        CustCntctDtls cmpnt = new CustCntctDtls();
        cmpnt.setCustomer(customer);
        cmpnt.setContactType(cntctType);
        cmpnt.setContactValue(value);
        cmpnt.setEffectiveDate(audit.getEffectiveDate());
        cmpnt.setCrudValue(audit.getCrudValue());
        cmpnt.setUserId(audit.getUserId());
        cmpnt.setWorkstationId(audit.getWorkstationId());
        cmpnt.setProgramId(audit.getProgramId());
        cmpnt.setHostTimestamp(audit.getHostTimestamp());
        cmpnt.setLocalTimestamp(audit.getLocalTimestamp());
        cmpnt.setAcceptanceTimestamp(audit.getAcceptanceTimestamp());
        cmpnt.setAcceptanceTimestampUtcOffset(audit.getAcceptanceTimestampUtcOffset());
        cmpnt.setUuid(audit.getUuid());
        custCntctDtlsRepository.save(cmpnt);
    }

    private String buildFullName(String first, String middle, String last) {
        StringBuilder sb = new StringBuilder();
        if (first != null && !first.isBlank()) sb.append(first.trim());
        if (middle != null && !middle.isBlank()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(middle.trim());
        }
        if (last != null && !last.isBlank()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(last.trim());
        }
        return sb.toString();
    }
}
