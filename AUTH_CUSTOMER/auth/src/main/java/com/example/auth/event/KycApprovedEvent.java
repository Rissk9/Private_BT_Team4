package com.example.auth.event;

import com.example.auth.entity.CustDtl;

/**
 * Spring application event raised from within the KYC submission transaction
 * after the customer has been persisted with an ACTIVE status.
 *
 * <p>Listeners register this with {@code @TransactionalEventListener(phase =
 * AFTER_COMMIT)} so the Kafka publish only happens once the database
 * transaction has actually committed. If the transaction rolls back, the
 * event is never raised and no Kafka message is sent.</p>
 */
public class KycApprovedEvent {

    private final CustDtl customer;

    public KycApprovedEvent(CustDtl customer) {
        this.customer = customer;
    }

    public CustDtl getCustomer() {
        return customer;
    }
}