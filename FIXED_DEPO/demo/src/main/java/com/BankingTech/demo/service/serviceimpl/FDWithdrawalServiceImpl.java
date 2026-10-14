
package com.BankingTech.demo.service.serviceimpl;

import com.BankingTech.demo.dto.requestdto.FDWithdrawalRequestDTO;
import com.BankingTech.demo.dto.responsedto.FDWithdrawalResponseDTO;
import com.BankingTech.demo.entity.FDInterestTransactionEntity;
import com.BankingTech.demo.entity.FixedDepositAccountEntity;
import com.BankingTech.demo.repository.FDInterestTransactionRepository;
import com.BankingTech.demo.repository.FixedDepositAccountRepository;
import com.BankingTech.demo.service.FDTransactionService;
import com.BankingTech.demo.service.FDWithdrawalService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class FDWithdrawalServiceImpl implements FDWithdrawalService {

        private static final BigDecimal PENALTY_RATE = new BigDecimal("0.01");

        private static final BigDecimal DAYS_PER_YEAR = new BigDecimal("365");

        private static final BigDecimal HUNDRED = new BigDecimal("100");

        private static final int MONEY_SCALE = 2;

        private final FixedDepositAccountRepository fixedDepositAccountRepository;

        private final FDInterestTransactionRepository fdInterestTransactionRepository;

        private final FDTransactionService fdTransactionService;

        public FDWithdrawalServiceImpl(
                        FixedDepositAccountRepository fixedDepositAccountRepository,
                        FDInterestTransactionRepository fdInterestTransactionRepository,
                        FDTransactionService fdTransactionService) {

                this.fixedDepositAccountRepository = fixedDepositAccountRepository;

                this.fdInterestTransactionRepository = fdInterestTransactionRepository;

                this.fdTransactionService = fdTransactionService;
        }

        @Override
        @Transactional
        public FDWithdrawalResponseDTO processWithdrawal(
                        FDWithdrawalRequestDTO request) {

                if (request == null
                                || request.getFdAccountNo() == null
                                || request.getFdAccountNo().isBlank()
                                || request.getWithdrawalDate() == null
                                || request.getTransferAccount() == null
                                || request.getTransferAccount().isBlank()) {

                        throw new IllegalArgumentException(
                                        "FD account, withdrawal date, and transfer account are required");
                }

                String fdAccountNo = request.getFdAccountNo().trim();
                LocalDate withdrawalDate = request.getWithdrawalDate();

                FixedDepositAccountEntity fd = fixedDepositAccountRepository
                                .findByFdAccountNo(fdAccountNo)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "FD account not found: "
                                                                + fdAccountNo));

                if (!"ACTIVE".equalsIgnoreCase(
                                fd.getFdAccountStatus())) {

                        throw new IllegalStateException(
                                        "Only ACTIVE FD accounts can be withdrawn");
                }

                LocalDateTime openedAt = fd.getFdAccountOpenedAt();

                if (openedAt == null) {
                        throw new IllegalStateException(
                                        "FD opening date is not available");
                }

                LocalDate openingDate = openedAt.toLocalDate();
                LocalDate maturityDate = fd.getFdAccountMaturityDate();
                LocalDate today = LocalDate.now();

                if (withdrawalDate.isBefore(openingDate)) {
                        throw new IllegalArgumentException(
                                        "Withdrawal date cannot be before the FD opening date");
                }

                if (withdrawalDate.isAfter(today)) {
                        throw new IllegalArgumentException(
                                        "Withdrawal date cannot be in the future");
                }

                if (maturityDate != null
                                && !withdrawalDate.isBefore(maturityDate)) {

                        throw new IllegalStateException(
                                        "This is not a premature withdrawal. "
                                                        + "Use the maturity settlement workflow.");
                }

                BigDecimal principal = requireAmount(
                                fd.getFdAccountPrincipal(),
                                "FD principal");

                BigDecimal rate = requireAmount(
                                fd.getFdAccountRate(),
                                "FD interest rate");

                BigDecimal accruedInterest = fd.getFdAccountAccruedInterest() == null
                                ? BigDecimal.ZERO
                                : fd.getFdAccountAccruedInterest();

                if (principal.signum() < 0
                                || accruedInterest.signum() < 0
                                || rate.signum() < 0) {

                        throw new IllegalStateException(
                                        "Principal, accrued interest, and rate cannot be negative");
                }

                /*
                 * Find the most recent successfully recorded interest date.
                 * Capitalized interest is already included in principal.
                 * Accrued, unpaid interest is held separately on the FD.
                 */
                List<FDInterestTransactionEntity> history = fdInterestTransactionRepository
                                .findByFdInterestTransactionFdAccountNoOrderByFdInterestTransactionDateDesc(
                                                fdAccountNo);

                LocalDate lastInterestDate = openingDate;

                if (history != null && !history.isEmpty()) {
                        FDInterestTransactionEntity latest = history.get(0);

                        if (latest.getFdInterestTransactionDate() != null) {
                                lastInterestDate = latest
                                                .getFdInterestTransactionDate()
                                                .toLocalDate();
                        }
                }

                if (lastInterestDate.isAfter(withdrawalDate)) {
                        throw new IllegalStateException(
                                        "Interest history contains a date after the withdrawal date");
                }

                long days = ChronoUnit.DAYS.between(
                                lastInterestDate, withdrawalDate);

                BigDecimal additionalInterest = calculateInterest(
                                principal,
                                accruedInterest,
                                rate,
                                days,
                                fd.getFdAccountInterestType(),
                                fd.getFdAccountInterestPayoutType());

                additionalInterest = additionalInterest.setScale(
                                MONEY_SCALE, RoundingMode.HALF_UP);

                BigDecimal totalEligibleInterest = accruedInterest
                                .add(additionalInterest)
                                .setScale(MONEY_SCALE, RoundingMode.HALF_UP);

                /*
                 * Provisional L15 rule:
                 * penalty = 1% of total eligible interest.
                 * Replace with product-configured rules when available.
                 */
                BigDecimal penalty = totalEligibleInterest
                                .multiply(PENALTY_RATE)
                                .setScale(MONEY_SCALE, RoundingMode.HALF_UP);

                /*
                 * Capitalized interest is already part of principal.
                 * Do not add it again as a separate interest amount.
                 */
                BigDecimal payoutInterest = "CAPITALIZE".equalsIgnoreCase(
                                fd.getFdAccountInterestPayoutType())
                                                ? additionalInterest
                                                : totalEligibleInterest;

                BigDecimal payoutBase = "CAPITALIZE".equalsIgnoreCase(
                                fd.getFdAccountInterestPayoutType())
                                                ? principal.add(additionalInterest)
                                                : principal.add(payoutInterest);

                BigDecimal withdrawalAmount = payoutBase
                                .subtract(penalty)
                                .setScale(MONEY_SCALE, RoundingMode.HALF_UP);

                if (withdrawalAmount.signum() < 0) {
                        throw new IllegalStateException(
                                        "Calculated withdrawal payout cannot be negative");
                }

                LocalDateTime now = LocalDateTime.now();

                fd.setFdAccountStatus("CLOSED");
                fd.setFdAccountClosedAt(withdrawalDate.atStartOfDay());

                /*
                 * Clear unpaid accrued interest after it is settled.
                 * Preserve the principal for audit/history purposes.
                 */
                fd.setFdAccountAccruedInterest(BigDecimal.ZERO);

                fd.setCrudValue("UPDATE");
                fd.setHostTs(now);
                fd.setLocalTs(now);

                fixedDepositAccountRepository.save(fd);

                fdTransactionService.recordTransaction(
                                fdAccountNo,
                                "WITHDRAWAL",
                                withdrawalAmount,
                                penalty,
                                withdrawalDate.atStartOfDay(),
                                "Premature FD withdrawal. Transfer account: "
                                                + request.getTransferAccount().trim());

                return new FDWithdrawalResponseDTO(
                                "success",
                                "FD account closed successfully.",
                                withdrawalAmount,
                                penalty);
        }

        private BigDecimal calculateInterest(
                        BigDecimal principal,
                        BigDecimal accruedInterest,
                        BigDecimal annualRate,
                        long days,
                        String interestType,
                        String payoutType) {

                if (days <= 0 || annualRate.signum() == 0) {
                        return BigDecimal.ZERO.setScale(MONEY_SCALE);
                }

                BigDecimal dailyRate = annualRate
                                .divide(HUNDRED, 12, RoundingMode.HALF_UP)
                                .divide(DAYS_PER_YEAR, 12, RoundingMode.HALF_UP);

                boolean compound = "COMPOUND".equalsIgnoreCase(interestType);

                boolean capitalize = "CAPITALIZE".equalsIgnoreCase(payoutType);

                BigDecimal interest = BigDecimal.ZERO;

                if (compound && capitalize) {
                        /*
                         * Capitalized interest is already reflected in principal.
                         * Compound the principal daily for the unprocessed period.
                         */
                        BigDecimal balance = principal;

                        for (long day = 0; day < days; day++) {
                                BigDecimal dailyInterest = balance
                                                .multiply(dailyRate)
                                                .setScale(MONEY_SCALE, RoundingMode.HALF_UP);

                                interest = interest.add(dailyInterest);
                                balance = balance.add(dailyInterest);
                        }

                        return interest.setScale(
                                        MONEY_SCALE, RoundingMode.HALF_UP);
                }

                BigDecimal interestBearingBalance = principal;

                if (compound && !capitalize) {
                        /*
                         * For a compound FD with unpaid accrued interest,
                         * include the existing accrued balance in the
                         * interest-bearing balance.
                         */
                        interestBearingBalance = principal.add(accruedInterest);
                }

                return interestBearingBalance
                                .multiply(annualRate)
                                .multiply(BigDecimal.valueOf(days))
                                .divide(
                                                HUNDRED.multiply(DAYS_PER_YEAR),
                                                MONEY_SCALE,
                                                RoundingMode.HALF_UP);
        }

        private BigDecimal requireAmount(
                        BigDecimal amount,
                        String fieldName) {

                if (amount == null) {
                        throw new IllegalStateException(
                                        fieldName + " is not available");
                }

                return amount;
        }
}
