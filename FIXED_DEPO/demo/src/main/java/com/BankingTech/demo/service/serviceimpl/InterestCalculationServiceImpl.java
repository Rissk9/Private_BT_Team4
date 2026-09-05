package com.BankingTech.demo.service.serviceimpl;

import com.BankingTech.demo.entity.FDInterestTransactionEntity;
import com.BankingTech.demo.entity.FixedDepositAccountEntity;
import com.BankingTech.demo.repository.FDInterestTransactionRepository;
import com.BankingTech.demo.repository.FixedDepositAccountRepository;
import com.BankingTech.demo.service.InterestCalculationService;
import com.BankingTech.demo.service.plugins.calculator.CompoundInterestCalculator;
import com.BankingTech.demo.service.plugins.calculator.SimpleInterestCalculator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service("interestCalculationService")
public class InterestCalculationServiceImpl
                implements InterestCalculationService {

        private final FixedDepositAccountRepository fixedDepositAccountRepository;

        private final FDInterestTransactionRepository fdInterestTransactionRepository;

        private final SimpleInterestCalculator simpleInterestCalculator;

        private final CompoundInterestCalculator compoundInterestCalculator;

        public InterestCalculationServiceImpl(
                        FixedDepositAccountRepository fixedDepositAccountRepository,
                        FDInterestTransactionRepository fdInterestTransactionRepository,
                        SimpleInterestCalculator simpleInterestCalculator,
                        CompoundInterestCalculator compoundInterestCalculator) {

                this.fixedDepositAccountRepository = fixedDepositAccountRepository;

                this.fdInterestTransactionRepository = fdInterestTransactionRepository;

                this.simpleInterestCalculator = simpleInterestCalculator;

                this.compoundInterestCalculator = compoundInterestCalculator;
        }

        @Override
        public void calculateInterestForActiveAccounts() {

                /*
                 * Retrieve all ACTIVE FD accounts.
                 */
                List<FixedDepositAccountEntity> accounts = fixedDepositAccountRepository
                                .findByFdAccountStatus("ACTIVE");

                for (FixedDepositAccountEntity account : accounts) {

                        try {

                                calculateInterestForAccount(
                                                account.getFdAccountNo());

                        } catch (Exception e) {

                                /*
                                 * L14 requirement:
                                 * If one FD fails, log the failure
                                 * and continue processing the others.
                                 */
                                System.err.println(
                                                "Interest calculation failed for FD "
                                                                + account.getFdAccountNo()
                                                                + ": "
                                                                + e.getMessage());
                        }
                }
        }

        @Override
        @Transactional
        public void calculateInterestForAccount(
                        String fdAccountNo) {

                FixedDepositAccountEntity fd = fixedDepositAccountRepository
                                .findByFdAccountNo(fdAccountNo)
                                .orElseThrow(
                                                () -> new IllegalArgumentException(
                                                                "FD account not found: "
                                                                                + fdAccountNo));

                /*
                 * Interest can only be calculated
                 * for ACTIVE accounts.
                 */
                if (!"ACTIVE".equalsIgnoreCase(
                                fd.getFdAccountStatus())) {

                        throw new IllegalStateException(
                                        "Interest can only be calculated for ACTIVE FD accounts");
                }

                /*
                 * Current calculation date.
                 */
                LocalDate today = LocalDate.now();

                LocalDateTime calculationDate = today.atStartOfDay();

                /*
                 * Idempotency:
                 *
                 * If interest has already been processed
                 * for this FD on this date, skip it.
                 */
                if (fdInterestTransactionRepository
                                .existsByFdInterestTransactionFdAccountNoAndFdInterestTransactionDate(
                                                fdAccountNo,
                                                calculationDate)) {

                        System.out.println(
                                        "Interest already processed for FD "
                                                        + fdAccountNo
                                                        + " on "
                                                        + today);

                        return;
                }

                /*
                 * Read the renamed FD account fields.
                 */
                BigDecimal principal = fd.getFdAccountPrincipal();

                BigDecimal rate = fd.getFdAccountRate();

                Integer termMonths = fd.getFdAccountTerm();

                /*
                 * Determine the calculation method from
                 * the FD's stored interest type.
                 */
                BigDecimal interest;

                if ("COMPOUND".equalsIgnoreCase(
                                fd.getFdAccountInterestType())) {

                        interest = compoundInterestCalculator.calculate(
                                        principal,
                                        rate,
                                        termMonths);

                } else if ("SIMPLE".equalsIgnoreCase(
                                fd.getFdAccountInterestType())) {

                        interest = simpleInterestCalculator.calculate(
                                        principal,
                                        rate,
                                        termMonths);

                } else {

                        throw new IllegalArgumentException(
                                        "Unsupported interest type: "
                                                        + fd.getFdAccountInterestType());
                }

                /*
                 * Accumulate newly calculated interest.
                 */
                BigDecimal currentAccruedInterest = fd.getFdAccountAccruedInterest() != null
                                ? fd.getFdAccountAccruedInterest()
                                : BigDecimal.ZERO;

                fd.setFdAccountAccruedInterest(
                                currentAccruedInterest.add(interest));

                /*
                 * CAPITALIZE
                 * -> add interest to principal
                 * -> clear accrued interest
                 *
                 * Other payout types
                 * -> retain interest as accrued
                 */
                boolean capitalized = "CAPITALIZE".equalsIgnoreCase(
                                fd.getFdAccountInterestPayoutType());

                if (capitalized) {

                        fd.setFdAccountPrincipal(
                                        fd.getFdAccountPrincipal()
                                                        .add(interest));

                        fd.setFdAccountAccruedInterest(
                                        BigDecimal.ZERO);
                }

                /*
                 * Save updated FD account.
                 */
                fixedDepositAccountRepository.save(fd);

                /*
                 * Create L14 interest transaction.
                 */
                FDInterestTransactionEntity transaction = new FDInterestTransactionEntity();

                transaction.setFdInterestTransactionId(
                                generateTransactionId());

                transaction.setFdInterestTransactionFdAccountNo(
                                fdAccountNo);

                transaction.setFdInterestTransactionInterestAmount(
                                interest);

                transaction.setFdInterestTransactionDate(
                                calculationDate);

                transaction.setFdInterestTransactionStatus(
                                capitalized
                                                ? "CAPITALIZED"
                                                : "ACCRUED");

                fdInterestTransactionRepository.save(
                                transaction);

                /*
                 * Console output useful for L14 testing.
                 */
                System.out.println(
                                "FD Account: "
                                                + fdAccountNo
                                                + " | Principal: "
                                                + principal
                                                + " | Interest: "
                                                + interest
                                                + " | New Principal: "
                                                + fd.getFdAccountPrincipal()
                                                + " | Capitalized: "
                                                + capitalized);
        }

        private String generateTransactionId() {

                return "FIT-"
                                + UUID.randomUUID()
                                                .toString()
                                                .replace("-", "")
                                                .substring(0, 16)
                                                .toUpperCase();
        }
}