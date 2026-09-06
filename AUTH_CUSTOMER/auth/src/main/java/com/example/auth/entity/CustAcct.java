package com.example.auth.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cust_acct")
public class CustAcct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CSTID_IDFR")
    private CustDtl customer;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "account_number", length = 30)
    private String accountNumber;

    @Column(name = "status", length = 30)
    private String status;

    @Column(name = "opened_on")
    private LocalDate openedOn;

    @Column(name = "closed_on")
    private LocalDate closedOn;

    @Column(name = "CSTACCT_EFCTV_DT")
    private LocalDate effectiveDate;

    @Column(name = "CSTACCT_CRUD_VALUE", length = 1)
    private String crudValue;

    @Column(name = "CSTACCT_USER_ID", length = 100)
    private String userId;

    @Column(name = "CSTACCT_WS_ID", length = 100)
    private String workstationId;

    @Column(name = "CSTACCT_PRGM_ID", length = 100)
    private String programId;

    @Column(name = "CSTACCT_HOST_TS")
    private LocalDateTime hostTimestamp;

    @Column(name = "CSTACCT_LOCAL_TS")
    private LocalDateTime localTimestamp;

    @Column(name = "CSTACCT_ACPT_TS")
    private LocalDateTime acceptanceTimestamp;

    @Column(name = "CSTACCT_ACPT_TS_UTC_OFS")
    private LocalDateTime acceptanceTimestampUtcOffset;

    @Column(name = "CSTACCT_UUID", length = 100)
    private String uuid;

 // Getters and setters
	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public CustDtl getCustomer() {
		return customer;
	}

	public void setCustomer(CustDtl customer) {
		this.customer = customer;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getOpenedOn() {
		return openedOn;
	}

	public void setOpenedOn(LocalDate openedOn) {
		this.openedOn = openedOn;
	}

	public LocalDate getClosedOn() {
		return closedOn;
	}

	public void setClosedOn(LocalDate closedOn) {
		this.closedOn = closedOn;
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