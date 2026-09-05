package com.BankingTech.demo.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fd_interest_transactions", uniqueConstraints = {
        @UniqueConstraint(name = "uk_fd_interest_transaction_account_date", columnNames = {
                "fd_interest_transaction_fd_account_no",
                "fd_interest_transaction_date"
        })
}, indexes = {
        @Index(name = "idx_fd_interest_transaction_account", columnList = "fd_interest_transaction_fd_account_no"),
        @Index(name = "idx_fd_interest_transaction_date", columnList = "fd_interest_transaction_date")
})
@AttributeOverrides({

        @AttributeOverride(name = "crudValue", column = @Column(name = "fd_interest_transaction_crud_value")),

        @AttributeOverride(name = "userId", column = @Column(name = "fd_interest_transaction_user_id")),

        @AttributeOverride(name = "wsId", column = @Column(name = "fd_interest_transaction_ws_id")),

        @AttributeOverride(name = "hostTs", column = @Column(name = "fd_interest_transaction_host_ts")),

        @AttributeOverride(name = "localTs", column = @Column(name = "fd_interest_transaction_local_ts")),

        @AttributeOverride(name = "ruleSystemId", column = @Column(name = "fd_interest_transaction_rule_system_id")),

        @AttributeOverride(name = "acptTs", column = @Column(name = "fd_interest_transaction_acpt_ts")),

        @AttributeOverride(name = "acptTsUtcOfst", column = @Column(name = "fd_interest_transaction_acpt_ts_utc_ofst")),

        @AttributeOverride(name = "uuid", column = @Column(name = "fd_interest_transaction_uuid"))
})
public class FDInterestTransactionEntity
        extends AuditEntity {

    @Id
    @Column(name = "fd_interest_transaction_id", nullable = false, unique = true, length = 50)
    private String fdInterestTransactionId;

    @Column(name = "fd_interest_transaction_fd_account_no", nullable = false, length = 30)
    private String fdInterestTransactionFdAccountNo;

    @Column(name = "fd_interest_transaction_interest_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal fdInterestTransactionInterestAmount;

    @Column(name = "fd_interest_transaction_date", nullable = false)
    private LocalDateTime fdInterestTransactionDate;

    @Column(name = "fd_interest_transaction_status", nullable = false, length = 20)
    private String fdInterestTransactionStatus;

    public String getFdInterestTransactionId() {
        return fdInterestTransactionId;
    }

    public void setFdInterestTransactionId(
            String fdInterestTransactionId) {
        this.fdInterestTransactionId = fdInterestTransactionId;
    }

    public String getFdInterestTransactionFdAccountNo() {
        return fdInterestTransactionFdAccountNo;
    }

    public void setFdInterestTransactionFdAccountNo(
            String fdInterestTransactionFdAccountNo) {
        this.fdInterestTransactionFdAccountNo = fdInterestTransactionFdAccountNo;
    }

    public BigDecimal getFdInterestTransactionInterestAmount() {
        return fdInterestTransactionInterestAmount;
    }

    public void setFdInterestTransactionInterestAmount(
            BigDecimal fdInterestTransactionInterestAmount) {
        this.fdInterestTransactionInterestAmount = fdInterestTransactionInterestAmount;
    }

    public LocalDateTime getFdInterestTransactionDate() {
        return fdInterestTransactionDate;
    }

    public void setFdInterestTransactionDate(
            LocalDateTime fdInterestTransactionDate) {
        this.fdInterestTransactionDate = fdInterestTransactionDate;
    }

    public String getFdInterestTransactionStatus() {
        return fdInterestTransactionStatus;
    }

    public void setFdInterestTransactionStatus(
            String fdInterestTransactionStatus) {
        this.fdInterestTransactionStatus = fdInterestTransactionStatus;
    }
}