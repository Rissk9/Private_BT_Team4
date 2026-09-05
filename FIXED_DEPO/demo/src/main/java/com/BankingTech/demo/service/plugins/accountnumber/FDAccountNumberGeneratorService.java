package com.BankingTech.demo.service.plugins.accountnumber;

import com.BankingTech.demo.repository.FixedDepositAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FDAccountNumberGeneratorService
                implements AccountNumberGenerator {

        private final FixedDepositAccountRepository fixedDepositAccountRepository;

        public FDAccountNumberGeneratorService(
                        FixedDepositAccountRepository fixedDepositAccountRepository) {
                this.fixedDepositAccountRepository = fixedDepositAccountRepository;
        }

        @Override
        @Transactional(readOnly = true)
        public synchronized String generate(String branchCode) {

                validateBranchCode(branchCode);

                Long currentSequence = fixedDepositAccountRepository
                                .findMaxSequenceByBranchCode(branchCode);

                long nextSequence = currentSequence + 1;

                if (nextSequence > 999999) {

                        throw new IllegalStateException(
                                        "FD account sequence exhausted for branch "
                                                        + branchCode);
                }

                String formattedSequence = String.format(
                                "%06d",
                                nextSequence);

                String checkDigit = calculateChecksum(
                                branchCode,
                                nextSequence);

                return branchCode
                                + formattedSequence
                                + checkDigit;
        }

        private void validateBranchCode(
                        String branchCode) {

                if (branchCode == null ||
                                !branchCode.matches("\\d{3}")) {

                        throw new IllegalArgumentException(
                                        "Branch code must contain exactly 3 digits");
                }
        }

        private String calculateChecksum(
                        String branchCode,
                        long sequence) {

                int sum = branchCode.hashCode()
                                + (int) sequence;

                return String.valueOf(
                                Math.abs(sum) % 9);
        }
}