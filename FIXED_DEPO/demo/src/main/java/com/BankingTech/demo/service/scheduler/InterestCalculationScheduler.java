package com.BankingTech.demo.service.scheduler;

import com.BankingTech.demo.service.InterestCalculationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InterestCalculationScheduler {

    private final InterestCalculationService interestCalculationService;

    public InterestCalculationScheduler(
            InterestCalculationService interestCalculationService) {
        this.interestCalculationService = interestCalculationService;
    }

    @Scheduled(fixedRate = 600000)
    public void runInterestCalculation() {

        System.out.println(
                "===== SCHEDULER IS RUNNING =====");

        interestCalculationService
                .calculateInterestForActiveAccounts();
    }
}