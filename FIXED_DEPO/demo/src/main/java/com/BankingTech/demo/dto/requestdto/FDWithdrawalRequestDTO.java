package com.BankingTech.demo.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class FDWithdrawalRequestDTO {

    @NotBlank(message = "FD account number is required")
    private String fdAccountNo;

    @NotNull(message = "Withdrawal date is required")
    private LocalDate withdrawalDate;

    @NotBlank(message = "Transfer account is required")
    private String transferAccount;

    public String getFdAccountNo() {
        return fdAccountNo;
    }

    public void setFdAccountNo(
            String fdAccountNo) {
        this.fdAccountNo = fdAccountNo;
    }

    public LocalDate getWithdrawalDate() {
        return withdrawalDate;
    }

    public void setWithdrawalDate(
            LocalDate withdrawalDate) {
        this.withdrawalDate = withdrawalDate;
    }

    public String getTransferAccount() {
        return transferAccount;
    }

    public void setTransferAccount(
            String transferAccount) {
        this.transferAccount = transferAccount;
    }
}