package com.example.auth.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cust_dtl")
public class CustDtl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CSTID_IDFR")
    private Long customerIdentifier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUST_TYPE")
    private CustTypMstr customerType;

    @Column(name = "CUST_FULL_NAME", length = 255)
    private String fullName;

    @Column(name = "CUST_DOB")
    private LocalDate dateOfBirth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUST_STATUS")
    private CustStsMstr customerStatus;

    @Column(name = "CUST_GENDR", length = 20)
    private String gender;

    @Column(name = "CUST_PRFRD_LNG", length = 50)
    private String preferredLanguage;

    @Column(name = "CUST_CNTRY_ORGTN", length = 100)
    private String countryOfOrigin;

    @Column(name = "CUST_EFCTV_DT")
    private LocalDate effectiveDate;

    @Column(name = "CUST_CRUD_VALUE", length = 1)
    private String crudValue;

    @Column(name = "CUST_USR_ID", length = 100)
    private String userId;

    @Column(name = "CUST_WS_ID", length = 100)
    private String workstationId;

    @Column(name = "CUST_PRGM_ID", length = 100)
    private String programId;

    @Column(name = "CUST_HOST_TS")
    private LocalDateTime hostTimestamp;

    @Column(name = "CUST_LOCAL_TS")
    private LocalDateTime localTimestamp;

    @Column(name = "CUST_ACPT_TS")
    private LocalDateTime acceptanceTimestamp;

    @Column(name = "CUST_ACPT_TS_UTC_OFST")
    private LocalDateTime acceptanceTimestampUtcOffset;

    @Column(name = "CUST_UUID", length = 100)
    private String uuid;

    // Getters and setters
	public Long getCustomerIdentifier() {
		return customerIdentifier;
	}

	public void setCustomerIdentifier(Long customerIdentifier) {
		this.customerIdentifier = customerIdentifier;
	}

	public CustTypMstr getCustomerType() {
		return customerType;
	}

	public void setCustomerType(CustTypMstr customerType) {
		this.customerType = customerType;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public CustStsMstr getCustomerStatus() {
		return customerStatus;
	}

	public void setCustomerStatus(CustStsMstr customerStatus) {
		this.customerStatus = customerStatus;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPreferredLanguage() {
		return preferredLanguage;
	}

	public void setPreferredLanguage(String preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}

	public String getCountryOfOrigin() {
		return countryOfOrigin;
	}

	public void setCountryOfOrigin(String countryOfOrigin) {
		this.countryOfOrigin = countryOfOrigin;
	}

	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getCrudValue() {
		return crudValue;
	}

	public void setCrudValue(String crudValue) {
		this.crudValue = crudValue;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getWorkstationId() {
		return workstationId;
	}

	public void setWorkstationId(String workstationId) {
		this.workstationId = workstationId;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public LocalDateTime getHostTimestamp() {
		return hostTimestamp;
	}

	public void setHostTimestamp(LocalDateTime hostTimestamp) {
		this.hostTimestamp = hostTimestamp;
	}

	public LocalDateTime getLocalTimestamp() {
		return localTimestamp;
	}

	public void setLocalTimestamp(LocalDateTime localTimestamp) {
		this.localTimestamp = localTimestamp;
	}

	public LocalDateTime getAcceptanceTimestamp() {
		return acceptanceTimestamp;
	}

	public void setAcceptanceTimestamp(LocalDateTime acceptanceTimestamp) {
		this.acceptanceTimestamp = acceptanceTimestamp;
	}

	public LocalDateTime getAcceptanceTimestampUtcOffset() {
		return acceptanceTimestampUtcOffset;
	}

	public void setAcceptanceTimestampUtcOffset(LocalDateTime acceptanceTimestampUtcOffset) {
		this.acceptanceTimestampUtcOffset = acceptanceTimestampUtcOffset;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}    
    
}