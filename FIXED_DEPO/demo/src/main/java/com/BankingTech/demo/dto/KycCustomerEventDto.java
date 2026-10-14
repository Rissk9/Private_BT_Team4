package com.BankingTech.demo.dto;

import java.time.LocalDateTime;

/**
 * DTO for the KYC event consumed from the {@code customer-kyc-events} topic.
 *
 * <p>Field names and types match the producer side so that the JSON
 * deserialiser can map the message directly into this class.</p>
 */
public class KycCustomerEventDto {

    private String eventId;
    private Long customerId;
    private String customerIdentifier;
    private String fullName;
    private String customerStatus;
    private String kycStatus;
    private LocalDateTime eventTimestamp;

    public KycCustomerEventDto() {
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerIdentifier() {
        return customerIdentifier;
    }

    public void setCustomerIdentifier(String customerIdentifier) {
        this.customerIdentifier = customerIdentifier;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    public String getKycStatus() {
        return kycStatus;
    }

    public void setKycStatus(String kycStatus) {
        this.kycStatus = kycStatus;
    }

    public LocalDateTime getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(LocalDateTime eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }
}