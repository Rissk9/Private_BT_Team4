package com.example.auth.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cust_addr_cmpnt")
public class CustAddrCmpnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CSTADCMP_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CSTID_IDFR")
    private CustDtl customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CSTADCMP_TYPE_ID")
    private AddrTypMstr addressType;

    @Column(name = "CSTADCMP_TYPE_VALUE", length = 255)
    private String addressTypeValue;

    @Column(name = "CSTADCMP_EFFCTV_DT")
    private LocalDate effectiveDate;

    @Column(name = "CSTADCMP_CRUD_VAL", length = 1)
    private String crudValue;

    @Column(name = "CSTADCMP_USR_ID", length = 100)
    private String userId;

    @Column(name = "CSTADCMP_WS_ID", length = 100)
    private String workstationId;

    @Column(name = "CSTADCMP_PRGM_ID", length = 100)
    private String programId;

    @Column(name = "CSTADCMP_HOST_TS")
    private LocalDateTime hostTimestamp;

    @Column(name = "CSTADCMP_LOCAL_TS")
    private LocalDateTime localTimestamp;

    @Column(name = "CSTADCMP_ACPT_TS")
    private LocalDateTime acceptanceTimestamp;

    @Column(name = "CSTADCMP_ACPT_TS_UTC_OFS")
    private LocalDateTime acceptanceTimestampUtcOffset;

    @Column(name = "CSTADCMP_UUID", length = 100)
    private String uuid;

    // Getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CustDtl getCustomer() {
		return customer;
	}

	public void setCustomer(CustDtl customer) {
		this.customer = customer;
	}

	public AddrTypMstr getAddressType() {
		return addressType;
	}

	public void setAddressType(AddrTypMstr addressType) {
		this.addressType = addressType;
	}

	public String getAddressTypeValue() {
		return addressTypeValue;
	}

	public void setAddressTypeValue(String addressTypeValue) {
		this.addressTypeValue = addressTypeValue;
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