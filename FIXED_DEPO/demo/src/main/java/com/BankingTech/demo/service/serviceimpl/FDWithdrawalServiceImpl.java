package com.BankingTech.demo.service.serviceimpl;

import com.BankingTech.demo.dto.requestdto.FDWithdrawalRequestDTO;
import com.BankingTech.demo.dto.responsedto.FDWithdrawalResponseDTO;
import com.BankingTech.demo.entity.FixedDepositAccountEntity;
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

@Service
public class FDWithdrawalServiceImpl
                implements FDWithdrawalService {

        private final FixedDepositAccountRepository fixedDepositAccountRepository;

        private final FDTransactionService fdTransactionService;

        public FDWithdrawalServiceImpl(
                        FixedDepositAccountRepository fixedDepositAccountRepository,
                        FDTransactionService fdTransactionService) {

                this.fixedDepositAccountRepository = fixedDepositAccountRepository;

                this.fdTransactionService = fdTransactionService;
        }

        @Override
        @Transactional
        public FDWithdrawalResponseDTO processWithdrawal(
                        FDWithdrawalRequestDTO request) {

                FixedDepositAccountEntity fd = fixedDepositAccountRepository
                                .findByFdAccountNo(
                                                request.getFdAccountNo())
                                .orElseThrow(
                                                () -> new IllegalArgumentException(
                                                                "FD account not found: "
                                                                                + request.getFdAccountNo()));

                /*
                 * Withdrawal is allowed only for ACTIVE accounts.
                 */
                if (!"ACTIVE".equalsIgnoreCase(
                                fd.getFdAccountStatus())) {

                        throw new IllegalStateException(
                                        "Only ACTIVE FD accounts can be withdrawn");
                }

                LocalDate withdrawalDate = request.getWithdrawalDate();

                /*
                 * For now, use the audit local timestamp
                 * as the FD start date.
                 *
                 * This is a temporary solution until the
                 * FD opening date becomes an explicit
                 * business field.
                 */
                LocalDateTime localTimestamp = fd.getLocalTs();

                if (localTimestamp == null) {

                        throw new IllegalStateException(
                                        "FD creation timestamp is not available");
                }

                LocalDate creationDate = localTimestamp.toLocalDate();

                /*
                 * Withdrawal date cannot be before
                 * the FD creation date.
                 */
                if (withdrawalDate.isBefore(
                                creationDate)) {

                        throw new IllegalArgumentException(
                                        "Withdrawal date cannot be before FD creation date");
                }

                /*
                 * Calculate elapsed months.
                 */
                long monthsElapsed = ChronoUnit.MONTHS.between(
                                creationDate,
                                withdrawalDate);

                /*
                 * Do not allow negative values.
                 */
                if (monthsElapsed < 0) {
                        monthsElapsed = 0;
                }

                /*
                 * Simple interest calculation until
                 * the withdrawal date.
                 *
                 * Interest =
                 *
                 * (Principal × Rate × MonthsElapsed)
                 * / (12 × 100)
                 */
                BigDecimal accruedInterest = fd.getFdAccountPrincipal()
                                .multiply(
                                                fd.getFdAccountRate())
                                .multiply(
                                                BigDecimal.valueOf(
                                                                monthsElapsed))
                                .divide(
                                                BigDecimal.valueOf(1200),
                                                2,
                                                RoundingMode.HALF_UP);

                /*
                 * Temporary penalty rule for L15:
                 *
                 * 1% of interest earned.
                 *
                 * Later this should come from
                 * product-specific configuration.
                 */
                BigDecimal penaltyRate = BigDecimal.ONE;

                BigDecimal penalty = accruedInterest
                                .multiply(penaltyRate)
                                .divide(
                                                BigDecimal.valueOf(100),
                                                2,
                                                RoundingMode.HALF_UP);

                /*
                 * Final withdrawal payout:
                 *
                 * Principal
                 * + earned interest
                 * - penalty
                 */
                BigDecimal withdrawalAmount = fd.getFdAccountPrincipal()
                                .add(accruedInterest)
                                .subtract(penalty);

                /*
                 * Close the FD account.
                 */
                fd.setFdAccountStatus(
                                "CLOSED");

                fd.setFdAccountClosedAt(
                                withdrawalDate.atStartOfDay());

                /*
                 * Update audit information.
                 */
                LocalDateTime now = LocalDateTime.now();

                fd.setCrudValue(
                                "UPDATE");

                fd.setHostTs(
                                now);

                fd.setLocalTs(
                                now);

                fixedDepositAccountRepository.save(
                                fd);

                /*
                 * Record the withdrawal transaction.
                 */
                fdTransactionService.recordTransaction(
                                fd.getFdAccountNo(),
                                "WITHDRAWAL",
                                withdrawalAmount,
                                penalty,
                                withdrawalDate.atStartOfDay(),
                                "Premature FD withdrawal. Transfer account: "
                                                + request.getTransferAccount());

                /*
                 * Build response.
                 */
                FDWithdrawalResponseDTO response = new FDWithdrawalResponseDTO();

                response.setStatus(
                                "success");

                response.setMessage(
                                "FD account closed successfully.");

                response.setWithdrawalAmount(
                                withdrawalAmount);

                response.setPenaltyApplied(
                                penalty);

                return response;
        }
}