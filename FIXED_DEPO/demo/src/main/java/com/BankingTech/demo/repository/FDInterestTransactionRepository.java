package com.BankingTech.demo.repository;

import com.BankingTech.demo.entity.FDInterestTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FDInterestTransactionRepository
                extends JpaRepository<FDInterestTransactionEntity, String> {

        Optional<FDInterestTransactionEntity> findByFdInterestTransactionId(
                        String fdInterestTransactionId);

        List<FDInterestTransactionEntity> findByFdInterestTransactionFdAccountNoOrderByFdInterestTransactionDateDesc(
                        String fdInterestTransactionFdAccountNo);

        boolean existsByFdInterestTransactionFdAccountNoAndFdInterestTransactionDate(
                        String fdInterestTransactionFdAccountNo,
                        LocalDateTime fdInterestTransactionDate);
}