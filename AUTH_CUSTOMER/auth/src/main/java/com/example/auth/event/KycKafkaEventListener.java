package com.example.auth.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.annotation.Transactional;

import com.example.auth.dto.KycCustomerEventDto;
import com.example.auth.entity.CustDtl;
import com.example.auth.service.KycEventPublisherService;

/**
 * Listens for {@link KycApprovedEvent} and publishes a Kafka message.
 *
 * <p>The listener is registered with {@code @TransactionalEventListener(phase =
 * AFTER_COMMIT)}, so the Kafka send only runs after the database transaction
 * that raised the event has committed. If the transaction is rolled back the
 * event is discarded and nothing is published.</p>
 */
@Component
public class KycKafkaEventListener {

    private final KycEventPublisherService kycEventPublisherService;

    public KycKafkaEventListener(KycEventPublisherService kycEventPublisherService) {
        this.kycEventPublisherService = kycEventPublisherService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onKycApproved(KycApprovedEvent event) {
        CustDtl customer = event.getCustomer();

        KycCustomerEventDto dto = new KycCustomerEventDto(
                java.util.UUID.randomUUID().toString(),
                customer.getCustomerIdentifier(),
                customer.getCustomerIdentifier().toString(),
                customer.getFullName(),
                customer.getCustomerStatus() != null ? customer.getCustomerStatus().getStatusCode() : null,
                "ACTIVE",
                java.time.LocalDateTime.now());

        kycEventPublisherService.publishKycEvent(dto);
    }
}