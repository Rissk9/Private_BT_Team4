package com.BankingTech.demo.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fd_accounts", indexes = {
        @Index(name = "idx_fd_account_customer_id", columnList = "fd_account_customer_id"),
        @Index(name = "idx_fd_account_product_code", columnList = "fd_account_product_code"),
        @Index(name = "idx_fd_account_status", columnList = "fd_account_status"),
        @Index(name = "idx_fd_account_maturity_date", columnList = "fd_account_maturity_date")
})
@AttributeOverrides({
        @AttributeOverride(name = "crudValue", column = @Column(name = "fd_account_crud_value")),
        @AttributeOverride(name = "userId", column = @Column(name = "fd_account_user_id")),
        @AttributeOverride(name = "wsId", column = @Column(name = "fd_account_ws_id")),
        @AttributeOverride(name = "hostTs", column = @Column(name = "fd_account_host_ts")),
        @AttributeOverride(name = "localTs", column = @Column(name = "fd_account_local_ts")),
        @AttributeOverride(name = "ruleSystemId", column = @Column(name = "fd_account_rule_system_id")),
        @AttributeOverride(name = "acptTs", column = @Column(name = "fd_account_acpt_ts")),
        @AttributeOverride(name = "acptTsUtcOfst", column = @Column(name = "fd_account_acpt_ts_utc_ofst")),
        @AttributeOverride(name = "uuid", column = @Column(name = "fd_account_uuid"))
})
public class FixedDepositAccountEntity extends AuditEntity {

    @Id
    @Column(name = "fd_account_no", nullable = false, unique = true, length = 30)
    private String fdAccountNo;

    @Column(name = "fd_account_customer_id", nullable = false, length = 30)
    private String fdAccountCustomerId;

    @Column(name = "fd_account_product_code", nullable = false, length = 30)
    private String fdAccountProductCode;

    @Column(name = "fd_account_principal", nullable = false, precision = 19, scale = 2)
    private BigDecimal fdAccountPrincipal;

    @Column(name = "fd_account_rate", nullable = false, precision = 7, scale = 4)
    private BigDecimal fdAccountRate;

    @Column(name = "fd_account_term", nullable = false)
    private Integer fdAccountTerm;

    @Column(name = "fd_account_opened_at", nullable = false)
    private LocalDateTime fdAccountOpenedAt;

    @Column(name = "fd_account_maturity_date", nullable = false)
    private LocalDate fdAccountMaturityDate;

    @Column(name = "fd_account_interest_type", nullable = false, length = 20)
    private String fdAccountInterestType;

    @Column(name = "fd_account_interest_payout_type", nullable = false, length = 20)
    private String fdAccountInterestPayoutType;

    // NEW: DAILY, MONTHLY, QUARTERLY, or YEARLY
    @Column(name = "fd_account_interest_frequency", nullable = false, length = 20)
    private String fdAccountInterestFrequency;

    @Column(name = "fd_account_accrued_interest", nullable = false, precision = 19, scale = 2)
    private BigDecimal fdAccountAccruedInterest = BigDecimal.ZERO;

    @Column(name = "fd_account_status", nullable = false, length = 30)
    private String fdAccountStatus;

    @Column(name = "fd_account_closed_at")
    private LocalDateTime fdAccountClosedAt;

    public String getFdAccountNo() {
        return fdAccountNo;
    }

    public void setFdAccountNo(String fdAccountNo) {
        this.fdAccountNo = fdAccountNo;
    }

    public String getFdAccountCustomerId() {
        return fdAccountCustomerId;
    }

    public void setFdAccountCustomerId(String fdAccountCustomerId) {
        this.fdAccountCustomerId = fdAccountCustomerId;
    }

    public String getFdAccountProductCode() {
        return fdAccountProductCode;
    }

    public void setFdAccountProductCode(String fdAccountProductCode) {
        this.fdAccountProductCode = fdAccountProductCode;
    }

    public BigDecimal getFdAccountPrincipal() {
        return fdAccountPrincipal;
    }

    public void setFdAccountPrincipal(BigDecimal fdAccountPrincipal) {
        this.fdAccountPrincipal = fdAccountPrincipal;
    }

    public BigDecimal getFdAccountRate() {
        return fdAccountRate;
    }

    public void setFdAccountRate(BigDecimal fdAccountRate) {
        this.fdAccountRate = fdAccountRate;
    }

    public Integer getFdAccountTerm() {
        return fdAccountTerm;
    }

    public void setFdAccountTerm(Integer fdAccountTerm) {
        this.fdAccountTerm = fdAccountTerm;
    }

    public LocalDateTime getFdAccountOpenedAt() {
        return fdAccountOpenedAt;
    }

    public void setFdAccountOpenedAt(LocalDateTime fdAccountOpenedAt) {
        this.fdAccountOpenedAt = fdAccountOpenedAt;
    }

    public LocalDate getFdAccountMaturityDate() {
        return fdAccountMaturityDate;
    }

    public void setFdAccountMaturityDate(LocalDate fdAccountMaturityDate) {
        this.fdAccountMaturityDate = fdAccountMaturityDate;
    }

    public String getFdAccountInterestType() {
        return fdAccountInterestType;
    }

    public void setFdAccountInterestType(String fdAccountInterestType) {
        this.fdAccountInterestType = fdAccountInterestType;
    }

    public String getFdAccountInterestPayoutType() {
        return fdAccountInterestPayoutType;
    }

    public void setFdAccountInterestPayoutType(
            String fdAccountInterestPayoutType) {
        this.fdAccountInterestPayoutType = fdAccountInterestPayoutType;
    }

    // NEW getter and setter
    public String getFdAccountInterestFrequency() {
        return fdAccountInterestFrequency;
    }

    public void setFdAccountInterestFrequency(
            String fdAccountInterestFrequency) {
        this.fdAccountInterestFrequency = fdAccountInterestFrequency;
    }

    public BigDecimal getFdAccountAccruedInterest() {
        return fdAccountAccruedInterest;
    }

    public void setFdAccountAccruedInterest(
            BigDecimal fdAccountAccruedInterest) {
        this.fdAccountAccruedInterest = fdAccountAccruedInterest;
    }

    public String getFdAccountStatus() {
        return fdAccountStatus;
    }

    public void setFdAccountStatus(String fdAccountStatus) {
        this.fdAccountStatus = fdAccountStatus;
    }

    public LocalDateTime getFdAccountClosedAt() {
        return fdAccountClosedAt;
    }

    public void setFdAccountClosedAt(LocalDateTime fdAccountClosedAt) {
        this.fdAccountClosedAt = fdAccountClosedAt;
    }
}