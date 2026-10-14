package com.example.auth.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.auth.dto.KycCustomerEventDto;

/**
 * Publishes KYC events to the {@code customer-kyc-events} Kafka topic.
 *
 * <p>This service is intentionally kept separate from the business logic so
 * that the publish call can be invoked from inside the committed KYC
 * transaction without entangling Kafka concerns with the customer service.
 */
@Service
public class KycEventPublisherService {

    private static final String TOPIC = "customer-kyc-events";

    private final KafkaTemplate<String, KycCustomerEventDto> kafkaTemplate;

    public KycEventPublisherService(
            KafkaTemplate<String, KycCustomerEventDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Publishes a single KYC event. The message is sent asynchronously; the
     * broker delivery happens after the caller's database transaction has
     * committed.
     */
    public void publishKycEvent(KycCustomerEventDto event) {
        kafkaTemplate.send(TOPIC, event.getCustomerIdentifier(), event);
    }
}