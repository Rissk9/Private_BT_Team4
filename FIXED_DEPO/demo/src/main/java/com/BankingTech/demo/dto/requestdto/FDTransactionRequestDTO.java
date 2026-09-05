package com.BankingTech.demo.dto.requestdto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FDTransactionRequestDTO {

    @NotBlank(message = "FD account number is required")
    private String fdAccountNo;

    @NotBlank(message = "Transaction type is required")
    private String txnType;

    @NotNull(message = "Transaction amount is required")
    @DecimalMin(value = "0.01", message = "Transaction amount must be greater than zero")
    private BigDecimal amount;

    /*
     * Penalty is mainly used for
     * premature withdrawal transactions.
     *
     * For transactions without a penalty,
     * send 0.
     */
    @NotNull(message = "Penalty amount is required")
    @DecimalMin(value = "0.0", message = "Penalty amount cannot be negative")
    private BigDecimal penaltyAmount;

    @NotNull(message = "Transaction date is required")
    private LocalDateTime txnDate;

    private String remarks;

    public String getFdAccountNo() {
        return fdAccountNo;
    }

    public void setFdAccountNo(
            String fdAccountNo) {
        this.fdAccountNo = fdAccountNo;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(
            String txnType) {
        this.txnType = txnType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(
            BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(
            BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public LocalDateTime getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(
            LocalDateTime txnDate) {
        this.txnDate = txnDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(
            String remarks) {
        this.remarks = remarks;
    }
}