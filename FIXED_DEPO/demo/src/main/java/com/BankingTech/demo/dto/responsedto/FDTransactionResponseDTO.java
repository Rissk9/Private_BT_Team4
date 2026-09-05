package com.BankingTech.demo.dto.responsedto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FDTransactionResponseDTO {

    private String transactionId;

    private String fdAccountNo;

    private String txnType;

    private BigDecimal amount;

    private LocalDateTime txnDate;

    private String remarks;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getFdAccountNo() {
        return fdAccountNo;
    }

    public void setFdAccountNo(String fdAccountNo) {
        this.fdAccountNo = fdAccountNo;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(LocalDateTime txnDate) {
        this.txnDate = txnDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}