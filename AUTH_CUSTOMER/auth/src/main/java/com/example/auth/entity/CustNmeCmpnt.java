package com.example.auth.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cust_nme_cmpnt")
public class CustNmeCmpnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CSTNMCMP_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CSTID_IDFR")
    private CustDtl customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CSTNMCMP_TYPE_ID")
    private NmeTypMstr nameType;

    @Column(name = "CSTNMCMP_TYPE_VALUE", length = 255)
    private String nameTypeValue;

    @Column(name = "CSTNMCMP_EFFCTV_DT")
    private LocalDate effectiveDate;

    @Column(name = "CSTNMCMP_CRUD_VAL", length = 1)
    private String crudValue;

    @Column(name = "CSTNMCMP_USR_ID", length = 100)
    private String userId;

    @Column(name = "CSTNMCMP_WS_ID", length = 100)
    private String workstationId;

    @Column(name = "CSTNMCMP_PRGM_ID", length = 100)
    private String programId;

    @Column(name = "CSTNMCMP_HOST_TS")
    private LocalDateTime hostTimestamp;

    @Column(name = "CSTNMCMP_LOCAL_TS")
    private LocalDateTime localTimestamp;

    @Column(name = "CSTNMCMP_ACPT_TS")
    private LocalDateTime acceptanceTimestamp;

    @Column(name = "CSTNMCMP_ACPT_TS_UTC_OFS")
    private LocalDateTime acceptanceTimestampUtcOffset;

    @Column(name = "CSTNMCMP_UUID", length = 100)
    private String uuid;

    //getters and setters
  
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

	public NmeTypMstr getNameType() {
		return nameType;
	}

	public void setNameType(NmeTypMstr nameType) {
		this.nameType = nameType;
	}

	public String getNameTypeValue() {
		return nameTypeValue;
	}

	public void setNameTypeValue(String nameTypeValue) {
		this.nameTypeValue = nameTypeValue;
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