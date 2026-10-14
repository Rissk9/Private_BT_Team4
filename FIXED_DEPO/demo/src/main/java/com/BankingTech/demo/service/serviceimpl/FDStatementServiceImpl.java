
package com.BankingTech.demo.service.serviceimpl;

import com.BankingTech.demo.entity.FDStatementEntity;
import com.BankingTech.demo.entity.FixedDepositAccountEntity;
import com.BankingTech.demo.repository.FDStatementRepository;
import com.BankingTech.demo.repository.FixedDepositAccountRepository;
import com.BankingTech.demo.service.FDStatementService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FDStatementServiceImpl implements FDStatementService {

        private final FDStatementRepository fdStatementRepository;
        private final FixedDepositAccountRepository fixedDepositAccountRepository;

        public FDStatementServiceImpl(
                        FDStatementRepository fdStatementRepository,
                        FixedDepositAccountRepository fixedDepositAccountRepository) {

                this.fdStatementRepository = fdStatementRepository;
                this.fixedDepositAccountRepository = fixedDepositAccountRepository;
        }

        /**
         * Generates a statement using the current system date.
         */
        @Override
        @Transactional
        public void generateStatement(String fdAccountNo) {
                generateStatement(fdAccountNo, LocalDate.now());
        }

        /**
         * Generates a statement for the specified business date.
         * If a statement already exists for that FD and date,
         * this method leaves the existing statement unchanged.
         */
        @Override
        @Transactional
        public void generateStatement(
                        String fdAccountNo,
                        LocalDate businessDate) {

                if (businessDate == null) {
                        throw new IllegalArgumentException(
                                        "Statement business date is required");
                }

                FixedDepositAccountEntity fd = fixedDepositAccountRepository
                                .findByFdAccountNo(fdAccountNo)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "FD account not found: " + fdAccountNo));

                LocalDateTime statementDate = businessDate.atStartOfDay();

                // Avoid duplicate statements for the same FD and business date.
                boolean statementExists = fdStatementRepository
                                .existsByFdStatementFdAccountNoAndFdStatementDate(
                                                fdAccountNo,
                                                statementDate);

                if (statementExists) {
                        System.out.println(
                                        "Statement already exists for FD "
                                                        + fdAccountNo
                                                        + " on " + businessDate);
                        return;
                }

                BigDecimal principal = fd.getFdAccountPrincipal() != null
                                ? fd.getFdAccountPrincipal()
                                : BigDecimal.ZERO;

                BigDecimal accruedInterest = fd.getFdAccountAccruedInterest() != null
                                ? fd.getFdAccountAccruedInterest()
                                : BigDecimal.ZERO;

                FDStatementEntity statement = new FDStatementEntity();

                statement.setFdStatementId(generateStatementId());
                statement.setFdStatementFdAccountNo(fdAccountNo);
                statement.setFdStatementDate(statementDate);
                statement.setFdStatementPrincipal(principal);
                statement.setFdStatementAccruedInterest(accruedInterest);

                // Follow the audit-field conventions used by your existing entity.
                LocalDateTime now = LocalDateTime.now();
                statement.setCrudValue("CREATE");
                statement.setHostTs(now);
                statement.setLocalTs(now);

                fdStatementRepository.save(statement);

                System.out.println(
                                "Statement generated for FD " + fdAccountNo
                                                + " | Date: " + businessDate
                                                + " | Principal: " + principal
                                                + " | Accrued Interest: " + accruedInterest);
        }

        private String generateStatementId() {
                return "FDS-"
                                + UUID.randomUUID()
                                                .toString()
                                                .replace("-", "")
                                                .substring(0, 16)
                                                .toUpperCase();
        }
}
