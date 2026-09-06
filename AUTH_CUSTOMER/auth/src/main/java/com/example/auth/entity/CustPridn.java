package com.example.auth.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cust_pridn")
public class CustPridn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CSTPRID_REC_ID")
    private Long recordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CSTID_IDFR")
    private CustDtl customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CSTCL_ID")
    private CustCl classification;

    @Column(name = "CSTPRID_VALUE", length = 255)
    private String value;

    @Column(name = "CSTPRID_STRT_DT")
    private LocalDate startDate;

    @Column(name = "CSTPRID_END_DT")
    private LocalDate endDate;

    @Column(name = "CSTPRID_EFFCTV_DT")
    private LocalDate effectiveDate;

    @Column(name = "CSTPRID_CRUD_VAL", length = 1)
    private String crudValue;

    @Column(name = "CSTPRID_USR_ID", length = 100)
    private String userId;

    @Column(name = "CSTPRID_WS_ID", length = 100)
    private String workstationId;

    @Column(name = "CSTPRID_PRGM_ID", length = 100)
    private String programId;

    @Column(name = "CSTPRID_HOST_TS")
    private LocalDateTime hostTimestamp;

    @Column(name = "CSTPRID_LOCAL_TS")
    private LocalDateTime localTimestamp;

    @Column(name = "CSTPRID_ACPT_TS")
    private LocalDateTime acceptanceTimestamp;

    @Column(name = "CSTPRID_ACPT_TS_UTC_OFST")
    private LocalDateTime acceptanceTimestampUtcOffset;

    @Column(name = "CSTPRID_UUID", length = 100)
    private String uuid;

    //getters and setters
    
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

	public CustCl getClassification() {
		return classification;
	}

	public void setClassification(CustCl classification) {
		this.classification = classification;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
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