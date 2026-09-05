package com.BankingTech.demo.dto.responsedto;

import java.math.BigDecimal;

public class FDWithdrawalResponseDTO {

    private String status;

    private String message;

    private BigDecimal withdrawalAmount;

    private BigDecimal penaltyApplied;

    public FDWithdrawalResponseDTO() {
    }

    public FDWithdrawalResponseDTO(
            String status,
            String message,
            BigDecimal withdrawalAmount,
            BigDecimal penaltyApplied) {

        this.status = status;
        this.message = message;
        this.withdrawalAmount = withdrawalAmount;
        this.penaltyApplied = penaltyApplied;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(
            String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(
            String message) {
        this.message = message;
    }

    public BigDecimal getWithdrawalAmount() {
        return withdrawalAmount;
    }

    public void setWithdrawalAmount(
            BigDecimal withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    public BigDecimal getPenaltyApplied() {
        return penaltyApplied;
    }

    public void setPenaltyApplied(
            BigDecimal penaltyApplied) {
        this.penaltyApplied = penaltyApplied;
    }
}