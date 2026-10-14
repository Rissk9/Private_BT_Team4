package com.BankingTech.demo.scheduler;

import com.BankingTech.demo.service.MaturityProcessingService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class FDMaturityBatchScheduler {

    private final MaturityProcessingService maturityProcessingService;

    public FDMaturityBatchScheduler(
            MaturityProcessingService maturityProcessingService) {

        this.maturityProcessingService = maturityProcessingService;
    }

    /*
     * Runs every day at 4:00 AM.
     */
    @Scheduled(cron = "0 0 4 * * ?")
    public void runMaturityBatch() {

        maturityProcessingService.processMaturedAccounts(
                LocalDate.now());
    }
}