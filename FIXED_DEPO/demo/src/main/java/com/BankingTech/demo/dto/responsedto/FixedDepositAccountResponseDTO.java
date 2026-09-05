package com.BankingTech.demo.dto.responsedto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class FixedDepositAccountResponseDTO {

    private String fdAccountNo;

    private String customerId;

    private String productCode;

    private BigDecimal principalAmount;

    private BigDecimal interestRate;

    private Integer termMonths;

    private String status;

    private String interestType;

    private String interestPayoutType;

    private BigDecimal accruedInterest;

    private LocalDateTime closedAt;

    /*
     * Audit information
     */
    private String crudValue;

    private String userId;

    private String wsId;

    private LocalDateTime hostTs;

    private LocalDateTime localTs;

    private String ruleSystemId;

    private LocalDateTime acptTs;

    private String acptTsUtcOfst;

    private UUID uuid;

    public String getFdAccountNo() {
        return fdAccountNo;
    }

    public void setFdAccountNo(String fdAccountNo) {
        this.fdAccountNo = fdAccountNo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(BigDecimal principalAmount) {
        this.principalAmount = principalAmount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getTermMonths() {
        return termMonths;
    }

    public void setTermMonths(Integer termMonths) {
        this.termMonths = termMonths;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public String getInterestPayoutType() {
        return interestPayoutType;
    }

    public void setInterestPayoutType(String interestPayoutType) {
        this.interestPayoutType = interestPayoutType;
    }

    public BigDecimal getAccruedInterest() {
        return accruedInterest;
    }

    public void setAccruedInterest(BigDecimal accruedInterest) {
        this.accruedInterest = accruedInterest;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
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

    public String getWsId() {
        return wsId;
    }

    public void setWsId(String wsId) {
        this.wsId = wsId;
    }

    public LocalDateTime getHostTs() {
        return hostTs;
    }

    public void setHostTs(LocalDateTime hostTs) {
        this.hostTs = hostTs;
    }

    public LocalDateTime getLocalTs() {
        return localTs;
    }

    public void setLocalTs(LocalDateTime localTs) {
        this.localTs = localTs;
    }

    public String getRuleSystemId() {
        return ruleSystemId;
    }

    public void setRuleSystemId(String ruleSystemId) {
        this.ruleSystemId = ruleSystemId;
    }

    public LocalDateTime getAcptTs() {
        return acptTs;
    }

    public void setAcptTs(LocalDateTime acptTs) {
        this.acptTs = acptTs;
    }

    public String getAcptTsUtcOfst() {
        return acptTsUtcOfst;
    }

    public void setAcptTsUtcOfst(String acptTsUtcOfst) {
        this.acptTsUtcOfst = acptTsUtcOfst;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}