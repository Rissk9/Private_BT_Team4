package com.example.auth.repository;

import com.example.auth.entity.CustAcct;
import com.example.auth.entity.CustDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustAcctRepository extends JpaRepository<CustAcct, Long> {

    Optional<CustAcct> findByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);

    List<CustAcct> findByCustomer(CustDtl customer);

    boolean existsByCustomerAndProductIdAndBranchIdAndStatusNot(
            CustDtl customer, Long productId, Long branchId, String status);
}
