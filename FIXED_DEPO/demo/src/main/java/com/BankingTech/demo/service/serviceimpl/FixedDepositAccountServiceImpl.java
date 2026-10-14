
package com.BankingTech.demo.service.serviceimpl;

import com.BankingTech.demo.dto.requestdto.FixedDepositAccountRequestDTO;
import com.BankingTech.demo.dto.responsedto.FDAccountCreationResponseDTO;
import com.BankingTech.demo.dto.responsedto.FixedDepositAccountResponseDTO;
import com.BankingTech.demo.entity.FDTransactionEntity;
import com.BankingTech.demo.entity.FixedDepositAccountEntity;
import com.BankingTech.demo.repository.FDTransactionRepository;
import com.BankingTech.demo.repository.FixedDepositAccountRepository;
import com.BankingTech.demo.service.FixedDepositAccountService;
import com.BankingTech.demo.service.plugins.accountnumber.AccountNumberGenerator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Service
public class FixedDepositAccountServiceImpl
                implements FixedDepositAccountService {

        private final FixedDepositAccountRepository fixedDepositAccountRepository;
        private final AccountNumberGenerator accountNumberGenerator;
        private final FDTransactionRepository fdTransactionRepository;

        public FixedDepositAccountServiceImpl(
                        FixedDepositAccountRepository fixedDepositAccountRepository,
                        AccountNumberGenerator accountNumberGenerator,
                        FDTransactionRepository fdTransactionRepository) {

                this.fixedDepositAccountRepository = fixedDepositAccountRepository;
                this.accountNumberGenerator = accountNumberGenerator;
                this.fdTransactionRepository = fdTransactionRepository;
        }

        @Override
        @Transactional
        public FixedDepositAccountResponseDTO createAccount(
                        FixedDepositAccountRequestDTO request) {

                String fdAccountNo = accountNumberGenerator.generate(
                                request.getBranchCode());

                if (fixedDepositAccountRepository.existsByFdAccountNo(fdAccountNo)) {
                        throw new IllegalStateException(
                                        "Generated FD account number already exists");
                }

                LocalDateTime now = LocalDateTime.now();

                FixedDepositAccountEntity entity = new FixedDepositAccountEntity();

                entity.setFdAccountNo(fdAccountNo);
                entity.setFdAccountCustomerId(request.getCustomerId());
                entity.setFdAccountProductCode(request.getProductCode());
                entity.setFdAccountPrincipal(request.getPrincipalAmount());
                entity.setFdAccountRate(request.getInterestRate());
                entity.setFdAccountTerm(request.getTermMonths());

                entity.setFdAccountOpenedAt(now);
                entity.setFdAccountMaturityDate(
                                now.toLocalDate().plusMonths(request.getTermMonths()));

                entity.setFdAccountStatus("ACTIVE");
                entity.setFdAccountInterestType(request.getInterestType());
                entity.setFdAccountInterestPayoutType(
                                request.getInterestPayoutType());

                entity.setFdAccountInterestFrequency(
                                request.getInterestFrequency());

                entity.setFdAccountAccruedInterest(BigDecimal.ZERO);

                setAccountAuditFields(entity, request.getCustomerId(), now);

                FixedDepositAccountEntity saved = fixedDepositAccountRepository.save(entity);

                return convertToResponseDTO(saved);
        }

        /*
         * L16:
         * Create FD account and automatically record
         * the initial DEPOSIT transaction.
         */
        @Override
        @Transactional
        public FDAccountCreationResponseDTO createAccountWithTransaction(
                        FixedDepositAccountRequestDTO request) {

                // 1. Generate FD account number.
                String fdAccountNo = accountNumberGenerator.generate(
                                request.getBranchCode());

                // 2. Check whether generated account number already exists.
                if (fixedDepositAccountRepository.existsByFdAccountNo(fdAccountNo)) {
                        throw new IllegalStateException(
                                        "Generated FD account number already exists");
                }

                LocalDateTime now = LocalDateTime.now();

                // 3. Create FD account.
                FixedDepositAccountEntity entity = new FixedDepositAccountEntity();

                entity.setFdAccountNo(fdAccountNo);
                entity.setFdAccountCustomerId(request.getCustomerId());
                entity.setFdAccountProductCode(request.getProductCode());
                entity.setFdAccountPrincipal(request.getPrincipalAmount());
                entity.setFdAccountRate(request.getInterestRate());
                entity.setFdAccountTerm(request.getTermMonths());

                entity.setFdAccountOpenedAt(now);
                entity.setFdAccountMaturityDate(
                                now.toLocalDate().plusMonths(request.getTermMonths()));

                entity.setFdAccountStatus("ACTIVE");
                entity.setFdAccountInterestType(request.getInterestType());
                entity.setFdAccountInterestPayoutType(
                                request.getInterestPayoutType());

                entity.setFdAccountInterestFrequency(
                                request.getInterestFrequency());

                entity.setFdAccountAccruedInterest(BigDecimal.ZERO);

                // 4. Set audit fields for FD account.
                setAccountAuditFields(entity, request.getCustomerId(), now);

                // 5. Save FD account.
                fixedDepositAccountRepository.save(entity);

                // 6. Create initial DEPOSIT transaction.
                FDTransactionEntity transaction = new FDTransactionEntity();

                transaction.setFdTransactionId(generateTransactionId());
                transaction.setFdTransactionFdAccountNo(fdAccountNo);
                transaction.setFdTransactionTxnType("DEPOSIT");
                transaction.setFdTransactionAmount(request.getPrincipalAmount());
                transaction.setFdTransactionPenaltyAmount(BigDecimal.ZERO);
                transaction.setFdTransactionTxnDate(now);
                transaction.setFdTransactionRemarks("Initial Deposit");

                // 7. Set audit fields for transaction.
                transaction.setCrudValue("CREATE");
                transaction.setUserId(request.getCustomerId());
                transaction.setWsId("BANKING_SYSTEM");
                transaction.setHostTs(now);
                transaction.setLocalTs(now);
                transaction.setRuleSystemId("FD_TRANSACTION");
                transaction.setAcptTs(now);
                transaction.setAcptTsUtcOfst(
                                ZoneOffset.systemDefault()
                                                .getRules()
                                                .getOffset(now)
                                                .toString());
                transaction.setUuid(UUID.randomUUID());

                // 8. Save initial DEPOSIT transaction.
                FDTransactionEntity savedTransaction = fdTransactionRepository.save(transaction);

                // 9. Build L16 response.
                FDAccountCreationResponseDTO response = new FDAccountCreationResponseDTO();

                response.setFdAccountNo(fdAccountNo);
                response.setInitialTransactionId(
                                savedTransaction.getFdTransactionId());
                response.setMessage("Account and initial deposit created.");

                return response;
        }

        /*
         * Sets audit fields for an FD account.
         */
        private void setAccountAuditFields(
                        FixedDepositAccountEntity entity,
                        String customerId,
                        LocalDateTime now) {

                entity.setCrudValue("CREATE");
                entity.setUserId(customerId);
                entity.setWsId("BANKING_SYSTEM");
                entity.setHostTs(now);
                entity.setLocalTs(now);
                entity.setRuleSystemId("FD_ACCOUNT");
                entity.setAcptTs(now);
                entity.setAcptTsUtcOfst(
                                ZoneOffset.systemDefault()
                                                .getRules()
                                                .getOffset(now)
                                                .toString());
                entity.setUuid(UUID.randomUUID());
        }

        /*
         * Generate unique transaction ID.
         */
        private String generateTransactionId() {
                return "FDT-"
                                + UUID.randomUUID()
                                                .toString()
                                                .replace("-", "")
                                                .substring(0, 16)
                                                .toUpperCase();
        }

        @Override
        @Transactional(readOnly = true)
        public FixedDepositAccountResponseDTO getAccount(String fdAccountNo) {

                FixedDepositAccountEntity entity = fixedDepositAccountRepository.findByFdAccountNo(fdAccountNo)
                                .orElseThrow(() -> new RuntimeException(
                                                "FD account not found: " + fdAccountNo));

                return convertToResponseDTO(entity);
        }

        @Override
        @Transactional(readOnly = true)
        public List<FixedDepositAccountResponseDTO> getAccountsByCustomer(
                        String customerId) {

                return fixedDepositAccountRepository
                                .findByFdAccountCustomerId(customerId)
                                .stream()
                                .map(this::convertToResponseDTO)
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public List<FixedDepositAccountResponseDTO> getAccountsByStatus(
                        String status) {

                return fixedDepositAccountRepository
                                .findByFdAccountStatus(status)
                                .stream()
                                .map(this::convertToResponseDTO)
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public List<FixedDepositAccountResponseDTO> getCustomerAccountsByStatus(
                        String customerId,
                        String status) {

                return fixedDepositAccountRepository
                                .findByFdAccountCustomerIdAndFdAccountStatus(
                                                customerId, status)
                                .stream()
                                .map(this::convertToResponseDTO)
                                .toList();
        }

        private FixedDepositAccountResponseDTO convertToResponseDTO(
                        FixedDepositAccountEntity entity) {

                FixedDepositAccountResponseDTO response = new FixedDepositAccountResponseDTO();

                response.setFdAccountNo(entity.getFdAccountNo());
                response.setCustomerId(entity.getFdAccountCustomerId());
                response.setProductCode(entity.getFdAccountProductCode());
                response.setPrincipalAmount(entity.getFdAccountPrincipal());
                response.setInterestRate(entity.getFdAccountRate());
                response.setTermMonths(entity.getFdAccountTerm());

                response.setOpenedAt(entity.getFdAccountOpenedAt());
                response.setMaturityDate(entity.getFdAccountMaturityDate());

                response.setStatus(entity.getFdAccountStatus());
                response.setInterestType(entity.getFdAccountInterestType());
                response.setInterestPayoutType(
                                entity.getFdAccountInterestPayoutType());

                response.setInterestFrequency(
                                entity.getFdAccountInterestFrequency());
                response.setAccruedInterest(entity.getFdAccountAccruedInterest());
                response.setClosedAt(entity.getFdAccountClosedAt());

                response.setCrudValue(entity.getCrudValue());
                response.setUserId(entity.getUserId());
                response.setWsId(entity.getWsId());
                response.setHostTs(entity.getHostTs());
                response.setLocalTs(entity.getLocalTs());
                response.setRuleSystemId(entity.getRuleSystemId());
                response.setAcptTs(entity.getAcptTs());
                response.setAcptTsUtcOfst(entity.getAcptTsUtcOfst());
                response.setUuid(entity.getUuid());

                return response;
        }
}
