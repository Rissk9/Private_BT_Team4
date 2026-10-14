
package com.BankingTech.demo.scheduler;

import com.BankingTech.demo.service.InterestCalculationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class FDInterestBatchScheduler {

        private final InterestCalculationService interestCalculationService;

        public FDInterestBatchScheduler(
                        InterestCalculationService interestCalculationService) {
                this.interestCalculationService = interestCalculationService;
        }

        /**
         * Runs every day at 3:00 AM.
         * The service determines whether interest is due for each FD.
         */
        @Scheduled(cron = "0 0 3 * * ?")
        public void runDailyInterestBatch() {
                runBatch(LocalDate.now());
        }

        /**
         * Allows controlled batch execution using a specified business date.
         */
        public void runBatch(LocalDate businessDate) {
                System.out.println(
                                "Starting FD interest batch for business date "
                                                + businessDate);

                try {
                        interestCalculationService
                                        .calculateInterestForActiveAccounts(businessDate);

                        System.out.println(
                                        "FD interest batch completed for " + businessDate);

                } catch (Exception e) {
                        System.err.println(
                                        "FD interest batch failed for "
                                                        + businessDate + ": " + e.getMessage());

                        throw new IllegalStateException(
                                        "FD interest batch failed", e);
                }
        }
}
