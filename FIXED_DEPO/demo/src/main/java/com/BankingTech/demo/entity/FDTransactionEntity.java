package com.BankingTech.demo.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fd_transactions", indexes = {
        @Index(name = "idx_fd_transaction_account", columnList = "fd_transaction_fd_account_no"),
        @Index(name = "idx_fd_transaction_type", columnList = "fd_transaction_txn_type"),
        @Index(name = "idx_fd_transaction_date", columnList = "fd_transaction_txn_date")
})
@AttributeOverrides({

        @AttributeOverride(name = "crudValue", column = @Column(name = "fd_transaction_crud_value")),

        @AttributeOverride(name = "userId", column = @Column(name = "fd_transaction_user_id")),

        @AttributeOverride(name = "wsId", column = @Column(name = "fd_transaction_ws_id")),

        @AttributeOverride(name = "hostTs", column = @Column(name = "fd_transaction_host_ts")),

        @AttributeOverride(name = "localTs", column = @Column(name = "fd_transaction_local_ts")),

        @AttributeOverride(name = "ruleSystemId", column = @Column(name = "fd_transaction_rule_system_id")),

        @AttributeOverride(name = "acptTs", column = @Column(name = "fd_transaction_acpt_ts")),

        @AttributeOverride(name = "acptTsUtcOfst", column = @Column(name = "fd_transaction_acpt_ts_utc_ofst")),

        @AttributeOverride(name = "uuid", column = @Column(name = "fd_transaction_uuid"))
})
public class FDTransactionEntity
        extends AuditEntity {

    @Id
    @Column(name = "fd_transaction_id", nullable = false, unique = true, length = 50)
    private String fdTransactionId;

    @Column(name = "fd_transaction_fd_account_no", nullable = false, length = 30)
    private String fdTransactionFdAccountNo;

    /*
     * Examples:
     *
     * WITHDRAWAL
     * DEPOSIT
     *
     * Future transaction types can also be
     * added when required by later labs.
     */
    @Column(name = "fd_transaction_txn_type", nullable = false, length = 30)
    private String fdTransactionTxnType;

    /*
     * Main transaction amount.
     *
     * For WITHDRAWAL:
     * final payout amount.
     *
     * For DEPOSIT:
     * deposited principal amount.
     */
    @Column(name = "fd_transaction_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal fdTransactionAmount;

    /*
     * Used mainly for premature withdrawals.
     *
     * For transactions where no penalty applies,
     * this will be zero.
     */
    @Column(name = "fd_transaction_penalty_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal fdTransactionPenaltyAmount = BigDecimal.ZERO;

    @Column(name = "fd_transaction_txn_date", nullable = false)
    private LocalDateTime fdTransactionTxnDate;

    /*
     * Additional transaction details.
     *
     * Example:
     * "Premature FD withdrawal"
     */
    @Column(name = "fd_transaction_remarks", length = 500)
    private String fdTransactionRemarks;

    public String getFdTransactionId() {
        return fdTransactionId;
    }

    public void setFdTransactionId(
            String fdTransactionId) {
        this.fdTransactionId = fdTransactionId;
    }

    public String getFdTransactionFdAccountNo() {
        return fdTransactionFdAccountNo;
    }

    public void setFdTransactionFdAccountNo(
            String fdTransactionFdAccountNo) {
        this.fdTransactionFdAccountNo = fdTransactionFdAccountNo;
    }

    public String getFdTransactionTxnType() {
        return fdTransactionTxnType;
    }

    public void setFdTransactionTxnType(
            String fdTransactionTxnType) {
        this.fdTransactionTxnType = fdTransactionTxnType;
    }

    public BigDecimal getFdTransactionAmount() {
        return fdTransactionAmount;
    }

    public void setFdTransactionAmount(
            BigDecimal fdTransactionAmount) {
        this.fdTransactionAmount = fdTransactionAmount;
    }

    public BigDecimal getFdTransactionPenaltyAmount() {
        return fdTransactionPenaltyAmount;
    }

    public void setFdTransactionPenaltyAmount(
            BigDecimal fdTransactionPenaltyAmount) {
        this.fdTransactionPenaltyAmount = fdTransactionPenaltyAmount;
    }

    public LocalDateTime getFdTransactionTxnDate() {
        return fdTransactionTxnDate;
    }

    public void setFdTransactionTxnDate(
            LocalDateTime fdTransactionTxnDate) {
        this.fdTransactionTxnDate = fdTransactionTxnDate;
    }

    public String getFdTransactionRemarks() {
        return fdTransactionRemarks;
    }

    public void setFdTransactionRemarks(
            String fdTransactionRemarks) {
        this.fdTransactionRemarks = fdTransactionRemarks;
    }
}