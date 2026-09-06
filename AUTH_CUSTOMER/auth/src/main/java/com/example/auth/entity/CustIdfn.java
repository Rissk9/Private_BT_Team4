package com.example.auth.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cust_idfn")
public class CustIdfn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CSTID_REC_ID")
    private Long recordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CSTID_IDFR")
    private CustDtl customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CSTCL_ID_TYPE")
    private IdfnTypMstr identificationType;

    @Column(name = "CSTID_ID_ITEM", length = 100)
    private String identificationItem;

    @Column(name = "CSTID_EFCTV_DT")
    private LocalDate effectiveDate;

    @Column(name = "CSTID_CRUD_VALUE", length = 1)
    private String crudValue;

    @Column(name = "CSTID_USER_ID", length = 100)
    private String userId;

    @Column(name = "CSTID_WS_ID", length = 100)
    private String workstationId;

    @Column(name = "CSTID_PRGM_ID", length = 100)
    private String programId;

    @Column(name = "CSTID_HOST_TS")
    private LocalDateTime hostTimestamp;

    @Column(name = "CSTID_LOCAL_TS")
    private LocalDateTime localTimestamp;

    @Column(name = "CSTID_ACPT_TS")
    private LocalDateTime acceptanceTimestamp;

    @Column(name = "CSTID_ACPT_TS_UTC_OFST")
    private LocalDateTime acceptanceTimestampUtcOffset;

    @Column(name = "CSTID_UUID", length = 100)
    private String uuid;


    // Getters and setters
	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public CustDtl getCustomer() {
		return customer;
	}

	public void setCustomer(CustDtl customer) {
		this.customer = customer;
	}

	public IdfnTypMstr getIdentificationType() {
		return identificationType;
	}

	public void setIdentificationType(IdfnTypMstr identificationType) {
		this.identificationType = identificationType;
	}

	public String getIdentificationItem() {
		return identificationItem;
	}

	public void setIdentificationItem(String identificationItem) {
		this.identificationItem = identificationItem;
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