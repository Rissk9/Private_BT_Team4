package com.BankingTech.demo.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.BankingTech.demo.dto.KycCustomerEventDto;
import com.BankingTech.demo.entity.FdCustomerRegistry;
import com.BankingTech.demo.repository.FdCustomerRegistryRepository;

/**
 * Consumes KYC events from the {@code customer-kyc-events} topic and
 * maintains the {@link FdCustomerRegistry} table.
 *
 * <p>The listener uses the consumer group {@code fd-service-group} so that,
 * should multiple instances of the FD service run, they share the workload
 * and each event is processed exactly once.</p>
 *
 * <p>Duplicate messages are handled safely by using Spring Data JPA's
 * {@code save} method, which performs an INSERT if the {@code customer_id}
 * does not yet exist or an UPDATE if it does.</p>
 */
@Service
public class KycEventConsumerService {

    private final FdCustomerRegistryRepository registryRepository;

    public KycEventConsumerService(FdCustomerRegistryRepository registryRepository) {
        this.registryRepository = registryRepository;
    }

    
    @KafkaListener(
            topics = "customer-kyc-events",
            groupId = "fd-service-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void consumeKycEvent(KycCustomerEventDto event) {
        System.out.println("========== KAFKA EVENT RECEIVED ==========");
        System.out.println("Event: " + event);

        if (event == null
                || event.getCustomerIdentifier() == null
                || event.getFullName() == null) {
            System.out.println("KAFKA EVENT SKIPPED: required fields are null");
            return;
        }

        FdCustomerRegistry registry = new FdCustomerRegistry(
                event.getCustomerIdentifier(),
                event.getFullName(),
                event.getCustomerStatus(),
                event.getKycStatus(),
                java.time.LocalDateTime.now());

        FdCustomerRegistry saved = registryRepository.save(registry);

        System.out.println("KAFKA EVENT SAVED: customer_id="
                + saved.getCustomerId());
    }

}