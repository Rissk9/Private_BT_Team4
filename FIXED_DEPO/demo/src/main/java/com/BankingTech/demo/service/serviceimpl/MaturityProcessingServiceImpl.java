package com.BankingTech.demo.service.serviceimpl;

import com.BankingTech.demo.entity.FixedDepositAccountEntity;
import com.BankingTech.demo.repository.FixedDepositAccountRepository;
import com.BankingTech.demo.service.EmailNotificationService;
import com.BankingTech.demo.service.FDTransactionService;
import com.BankingTech.demo.service.MaturityProcessingService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MaturityProcessingServiceImpl
        implements MaturityProcessingService {

    private final FixedDepositAccountRepository fixedDepositAccountRepository;
    private final FDTransactionService fdTransactionService;
    private final EmailNotificationService emailNotificationService;

    public MaturityProcessingServiceImpl(
            FixedDepositAccountRepository fixedDepositAccountRepository,
            FDTransactionService fdTransactionService,
            EmailNotificationService emailNotificationService) {

        this.fixedDepositAccountRepository = fixedDepositAccountRepository;

        this.fdTransactionService = fdTransactionService;

        this.emailNotificationService = emailNotificationService;
    }

    @Override
    public void processMaturedAccounts(LocalDate businessDate) {

        System.out.println(
                "==============================================");

        System.out.println(
                "FD MATURITY BATCH STARTED: "
                        + LocalDateTime.now());

        System.out.println(
                "Business Date: " + businessDate);

        List<FixedDepositAccountEntity> maturedAccounts = fixedDepositAccountRepository
                .findByFdAccountStatusAndFdAccountMaturityDateLessThanEqual(
                        "ACTIVE",
                        businessDate);

        System.out.println(
                "Matured Accounts Found: "
                        + maturedAccounts.size());

        int processed = 0;
        int failed = 0;

        for (FixedDepositAccountEntity fd : maturedAccounts) {

            try {

                processSingleMaturedAccount(
                        fd,
                        businessDate);

                processed++;

            } catch (Exception e) {

                failed++;

                System.err.println(
                        "Maturity processing failed for FD "
                                + fd.getFdAccountNo()
                                + ": "
                                + e.getMessage());
            }
        }

        System.out.println(
                "FD MATURITY BATCH COMPLETED: "
                        + LocalDateTime.now());

        System.out.println(
                "Processed: " + processed
                        + " | Failed: " + failed);

        System.out.println(
                "==============================================");
    }

    @Transactional
    protected void processSingleMaturedAccount(
            FixedDepositAccountEntity fd,
            LocalDate businessDate) {

        /*
         * Double-check account status.
         */
        if (!"ACTIVE".equalsIgnoreCase(
                fd.getFdAccountStatus())) {

            throw new IllegalStateException(
                    "FD is not ACTIVE: "
                            + fd.getFdAccountNo());
        }

        /*
         * Calculate final maturity amount.
         *
         * Maturity Amount =
         * Principal + Accrued Interest
         */
        BigDecimal maturityAmount = fd.getFdAccountPrincipal()
                .add(
                        fd.getFdAccountAccruedInterest());

        /*
         * Record MATURITY_PAYOUT transaction.
         */
        fdTransactionService.recordTransaction(
                fd.getFdAccountNo(),
                "MATURITY_PAYOUT",
                maturityAmount,
                BigDecimal.ZERO,
                LocalDateTime.now(),
                "Maturity payout on "
                        + businessDate);

        /*
         * Close the FD.
         */
        fd.setFdAccountStatus("CLOSED");
        fd.setFdAccountClosedAt(LocalDateTime.now());

        /*
         * Final accrued interest has now been processed.
         */
        fd.setFdAccountAccruedInterest(
                BigDecimal.ZERO);

        fixedDepositAccountRepository.save(fd);

        System.out.println(
                "FD "
                        + fd.getFdAccountNo()
                        + " matured for ₹"
                        + maturityAmount);

        /*
         * Email notification.
         *
         * IMPORTANT:
         * The exact customer/email lookup must come from
         * your Customer/User module.
         */
        emailNotificationService.sendMaturityNotification(
                fd.getFdAccountCustomerId(),
                fd.getFdAccountNo(),
                getCustomerEmail(
                        fd.getFdAccountCustomerId()),
                maturityAmount,
                fd.getFdAccountMaturityDate());
    }

    /*
     * Temporary placeholder until the exact Customer
     * entity/repository used by this project is wired in.
     */
    private String getCustomerEmail(String customerId) {

        /*
         * DO NOT leave this hard-coded in the final version.
         *
         * This method will be replaced by the actual
         * CustomerRepository lookup once the Customer
         * module's exact entity is confirmed.
         */
        throw new IllegalStateException(
                "Customer email lookup is not configured for customer: "
                        + customerId);
    }
}