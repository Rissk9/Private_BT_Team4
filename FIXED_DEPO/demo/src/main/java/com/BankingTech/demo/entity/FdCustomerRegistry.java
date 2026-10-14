package com.BankingTech.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Registry of customers whose KYC has been validated and marked ACTIVE in
 * the Auth & Customer module. Populated via Kafka consumer listening to
 * {@code customer-kyc-events}.
 *
 * <p>The primary key mirrors the {@code fd_account_customer_id} column in
 * {@link FixedDepositAccountEntity} so the same string-valued customer ID
 * can be used for both entities.</p>
 */
@Entity
@Table(name = "fd_customer_registry")
public class FdCustomerRegistry {

    @Id
    @Column(name = "customer_id", length = 30, nullable = false)
    private String customerId;

    @Column(name = "full_name", length = 150)
    private String fullName;

    @Column(name = "customer_status", length = 30)
    private String customerStatus;

    @Column(name = "kyc_status", length = 30)
    private String kycStatus;

    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;

    public FdCustomerRegistry() {
    }

    public FdCustomerRegistry(String customerId, String fullName, String customerStatus,
            String kycStatus, LocalDateTime lastUpdatedAt) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.customerStatus = customerStatus;
        this.kycStatus = kycStatus;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }
}