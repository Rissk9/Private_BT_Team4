package com.example.auth.dto;

import java.time.LocalDateTime;

/**
 * Event published to Kafka topic {@code customer-kyc-events} whenever a
 * customer's KYC submission is validated and their status transitions to
 * {@code ACTIVE}.
 *
 * <p>The field values are taken directly from the persisted entities so the
 * consumer can map them onto the FD registry without any translation.</p>
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

    public KycCustomerEventDto(String eventId, Long customerId, String customerIdentifier,
            String fullName, String customerStatus, String kycStatus, LocalDateTime eventTimestamp) {
        this.eventId = eventId;
        this.customerId = customerId;
        this.customerIdentifier = customerIdentifier;
        this.fullName = fullName;
        this.customerStatus = customerStatus;
        this.kycStatus = kycStatus;
        this.eventTimestamp = eventTimestamp;
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