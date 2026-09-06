package com.example.auth.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cust_cntct_dtls")
public class CustCntctDtls {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CSTCNDT_REC_ID")
    private Long recordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CSTID_IDFR")
    private CustDtl customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CSTCNDT_ID")
    private CntctTypMstr contactType;

    @Column(name = "CSTCNDT_VALUE", length = 255)
    private String contactValue;

    @Column(name = "CSTCNDT_STRT_DT")
    private LocalDate startDate;

    @Column(name = "CSTCNDT_END_DT")
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

	public CntctTypMstr getContactType() {
		return contactType;
	}

	public void setContactType(CntctTypMstr contactType) {
		this.contactType = contactType;
	}

	public String getContactValue() {
		return contactValue;
	}

	public void setContactValue(String contactValue) {
		this.contactValue = contactValue;
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