package com.BankingTech.demo.dto.responsedto;

public class FDAccountCreationResponseDTO {

    private String fdAccountNo;
    private String initialTransactionId;
    private String message;

    public String getFdAccountNo() {
        return fdAccountNo;
    }

    public void setFdAccountNo(String fdAccountNo) {
        this.fdAccountNo = fdAccountNo;
    }

    public String getInitialTransactionId() {
        return initialTransactionId;
    }

    public void setInitialTransactionId(String initialTransactionId) {
        this.initialTransactionId = initialTransactionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}