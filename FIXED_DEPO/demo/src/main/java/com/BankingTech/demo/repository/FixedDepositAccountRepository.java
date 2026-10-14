
package com.BankingTech.demo.repository;

import com.BankingTech.demo.entity.FixedDepositAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FixedDepositAccountRepository
                extends JpaRepository<FixedDepositAccountEntity, String> {

        Optional<FixedDepositAccountEntity> findByFdAccountNo(
                        String fdAccountNo);

        boolean existsByFdAccountNo(
                        String fdAccountNo);

        List<FixedDepositAccountEntity> findByFdAccountCustomerId(
                        String customerId);

        List<FixedDepositAccountEntity> findByFdAccountProductCode(
                        String productCode);

        List<FixedDepositAccountEntity> findByFdAccountStatus(
                        String status);

        List<FixedDepositAccountEntity> findByFdAccountCustomerIdAndFdAccountStatus(
                        String customerId,
                        String status);

        // Find active FDs whose maturity date has arrived.
        List<FixedDepositAccountEntity> findByFdAccountStatusAndFdAccountMaturityDateLessThanEqual(
                        String status,
                        LocalDate maturityDate);

        @Query(value = """
                        SELECT COALESCE(
                            MAX(
                                CAST(
                                    SUBSTRING(fd_account_no, 4, 6)
                                    AS UNSIGNED
                                )
                            ),
                            0
                        )
                        FROM fd_accounts
                        WHERE fd_account_no LIKE CONCAT(:branchCode, '%')
                        """, nativeQuery = true)
        Long findMaxSequenceByBranchCode(
                        @Param("branchCode") String branchCode);
}
