package com.BankingTech.demo.service.serviceimpl;

import com.BankingTech.demo.entity.FDInterestTransactionEntity;
import com.BankingTech.demo.entity.FixedDepositAccountEntity;
import com.BankingTech.demo.repository.FDInterestTransactionRepository;
import com.BankingTech.demo.repository.FixedDepositAccountRepository;
import com.BankingTech.demo.service.FDStatementService;
import com.BankingTech.demo.service.InterestCalculationService;
import com.BankingTech.demo.service.plugins.calculator.CompoundInterestCalculator;
import com.BankingTech.demo.service.plugins.calculator.SimpleInterestCalculator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service("interestCalculationService")
public class InterestCalculationServiceImpl
                implements InterestCalculationService {

        private static final BigDecimal HUNDRED = new BigDecimal("100");
        private static final BigDecimal DAYS_IN_YEAR = new BigDecimal("365");
        private static final int MONEY_SCALE = 2;

        private final FixedDepositAccountRepository accountRepository;
        private final FDInterestTransactionRepository interestTransactionRepository;
        private final SimpleInterestCalculator simpleInterestCalculator;
        private final CompoundInterestCalculator compoundInterestCalculator;
        private final FDStatementService fdStatementService;

        public InterestCalculationServiceImpl(
                        FixedDepositAccountRepository accountRepository,
                        FDInterestTransactionRepository interestTransactionRepository,
                        SimpleInterestCalculator simpleInterestCalculator,
                        CompoundInterestCalculator compoundInterestCalculator,
                        FDStatementService fdStatementService) {

                this.accountRepository = accountRepository;
                this.interestTransactionRepository = interestTransactionRepository;
                this.simpleInterestCalculator = simpleInterestCalculator;
                this.compoundInterestCalculator = compoundInterestCalculator;
                this.fdStatementService = fdStatementService;
        }

        @Override
        public void calculateInterestForActiveAccounts() {
                calculateInterestForActiveAccounts(LocalDate.now());
        }

        @Override
        public void calculateInterestForActiveAccounts(LocalDate businessDate) {
                if (businessDate == null) {
                        throw new IllegalArgumentException("Business date is required");
                }

                List<FixedDepositAccountEntity> accounts = accountRepository.findByFdAccountStatus("ACTIVE");

                for (FixedDepositAccountEntity account : accounts) {
                        try {
                                calculateInterestForAccount(
                                                account.getFdAccountNo(),
                                                businessDate);
                        } catch (Exception e) {
                                System.err.println(
                                                "Interest calculation failed for FD "
                                                                + account.getFdAccountNo()
                                                                + " on " + businessDate
                                                                + ": " + e.getMessage());
                        }
                }
        }

        @Override
        public void calculateInterestForAccount(String fdAccountNo) {
                calculateInterestForAccount(fdAccountNo, LocalDate.now());
        }

        @Override
        @Transactional
        public void calculateInterestForAccount(
                        String fdAccountNo,
                        LocalDate businessDate) {

                if (businessDate == null) {
                        throw new IllegalArgumentException("Business date is required");
                }

                FixedDepositAccountEntity fd = accountRepository
                                .findByFdAccountNo(fdAccountNo)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "FD account not found: " + fdAccountNo));

                if (!"ACTIVE".equalsIgnoreCase(fd.getFdAccountStatus())) {
                        throw new IllegalStateException(
                                        "Interest can only be calculated for ACTIVE FD accounts");
                }

                LocalDate openedDate = fd.getFdAccountOpenedAt() != null
                                ? fd.getFdAccountOpenedAt().toLocalDate()
                                : (fd.getLocalTs() == null
                                                ? null
                                                : fd.getLocalTs().toLocalDate());

                if (openedDate == null) {
                        throw new IllegalStateException(
                                        "FD opening date is unavailable");
                }

                if (businessDate.isBefore(openedDate)) {
                        throw new IllegalArgumentException(
                                        "Business date cannot precede FD opening date");
                }

                if (fd.getFdAccountMaturityDate() != null
                                && businessDate.isAfter(fd.getFdAccountMaturityDate())) {
                        businessDate = fd.getFdAccountMaturityDate();
                }

                List<FDInterestTransactionEntity> history = interestTransactionRepository
                                .findByFdInterestTransactionFdAccountNoOrderByFdInterestTransactionDateDesc(
                                                fdAccountNo);

                LocalDate lastPostingDate = history.isEmpty()
                                ? openedDate
                                : history.get(0)
                                                .getFdInterestTransactionDate()
                                                .toLocalDate();

                if (!businessDate.isAfter(lastPostingDate)) {
                        return;
                }

                if (!isPostingDue(
                                fd.getFdAccountInterestFrequency(),
                                lastPostingDate,
                                businessDate)) {
                        return;
                }

                long days = ChronoUnit.DAYS.between(
                                lastPostingDate,
                                businessDate);

                if (days <= 0) {
                        return;
                }

                BigDecimal principal = fd.getFdAccountPrincipal();
                BigDecimal rate = fd.getFdAccountRate();

                BigDecimal interest = calculatePeriodInterest(
                                fd,
                                principal,
                                rate,
                                days);

                if (interest.signum() <= 0) {
                        return;
                }

                boolean capitalize = "CAPITALIZE".equalsIgnoreCase(
                                fd.getFdAccountInterestPayoutType());

                BigDecimal existingAccrued = zeroIfNull(fd.getFdAccountAccruedInterest());

                if (capitalize) {
                        fd.setFdAccountPrincipal(
                                        principal.add(interest)
                                                        .setScale(MONEY_SCALE, RoundingMode.HALF_UP));

                        fd.setFdAccountAccruedInterest(
                                        BigDecimal.ZERO.setScale(
                                                        MONEY_SCALE,
                                                        RoundingMode.HALF_UP));
                } else {
                        fd.setFdAccountAccruedInterest(
                                        existingAccrued.add(interest)
                                                        .setScale(MONEY_SCALE, RoundingMode.HALF_UP));
                }

                accountRepository.save(fd);

                FDInterestTransactionEntity transaction = new FDInterestTransactionEntity();

                transaction.setFdInterestTransactionId(
                                generateTransactionId());

                transaction.setFdInterestTransactionFdAccountNo(fdAccountNo);
                transaction.setFdInterestTransactionInterestAmount(interest);

                // Use the requested business date, not LocalDate.now().
                transaction.setFdInterestTransactionDate(
                                businessDate.atStartOfDay());

                transaction.setFdInterestTransactionStatus(
                                capitalize ? "CAPITALIZED" : "ACCRUED");

                interestTransactionRepository.save(transaction);

                // Generate the statement for the same business date.
                fdStatementService.generateStatement(fdAccountNo, businessDate);

                System.out.println(
                                "FD " + fdAccountNo
                                                + " | period=" + lastPostingDate
                                                + " to " + businessDate
                                                + " | days=" + days
                                                + " | interest=" + interest
                                                + " | capitalized=" + capitalize);
        }

        private boolean isPostingDue(
                        String frequency,
                        LocalDate lastPostingDate,
                        LocalDate businessDate) {

                String value = frequency == null
                                ? "MONTHLY"
                                : frequency.trim().toUpperCase();

                return switch (value) {
                        case "DAILY" -> true;
                        case "MONTHLY" ->
                                !businessDate.isBefore(lastPostingDate.plusMonths(1));
                        case "QUARTERLY" ->
                                !businessDate.isBefore(lastPostingDate.plusMonths(3));
                        case "YEARLY", "ANNUALLY" ->
                                !businessDate.isBefore(lastPostingDate.plusYears(1));
                        default -> throw new IllegalArgumentException(
                                        "Unsupported FD interest frequency: " + frequency);
                };
        }

        private BigDecimal calculatePeriodInterest(
                        FixedDepositAccountEntity fd,
                        BigDecimal principal,
                        BigDecimal rate,
                        long days) {

                BigDecimal dailyRate = rate
                                .divide(HUNDRED, 12, RoundingMode.HALF_UP)
                                .divide(DAYS_IN_YEAR, 12, RoundingMode.HALF_UP);

                if ("COMPOUND".equalsIgnoreCase(fd.getFdAccountInterestType())) {
                        BigDecimal balance = principal.add(
                                        zeroIfNull(fd.getFdAccountAccruedInterest()));

                        for (long day = 0; day < days; day++) {
                                BigDecimal dailyInterest = balance
                                                .multiply(dailyRate)
                                                .setScale(MONEY_SCALE, RoundingMode.HALF_UP);

                                balance = balance.add(dailyInterest);
                        }

                        return balance.subtract(
                                        principal.add(
                                                        zeroIfNull(fd.getFdAccountAccruedInterest())))
                                        .setScale(MONEY_SCALE, RoundingMode.HALF_UP);
                }

                if ("SIMPLE".equalsIgnoreCase(fd.getFdAccountInterestType())) {
                        return principal.multiply(rate)
                                        .multiply(BigDecimal.valueOf(days))
                                        .divide(
                                                        HUNDRED.multiply(DAYS_IN_YEAR),
                                                        MONEY_SCALE,
                                                        RoundingMode.HALF_UP);
                }

                throw new IllegalArgumentException(
                                "Unsupported interest type: "
                                                + fd.getFdAccountInterestType());
        }

        private BigDecimal zeroIfNull(BigDecimal value) {
                return value == null ? BigDecimal.ZERO : value;
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