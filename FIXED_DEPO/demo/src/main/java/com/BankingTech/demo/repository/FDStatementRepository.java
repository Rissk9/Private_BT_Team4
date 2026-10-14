package com.BankingTech.demo.repository;

import com.BankingTech.demo.entity.FDStatementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FDStatementRepository extends JpaRepository<FDStatementEntity, String> {

    Optional<FDStatementEntity> findByFdStatementId(String fdStatementId);

    List<FDStatementEntity> findByFdStatementFdAccountNoOrderByFdStatementDateDesc(
            String fdAccountNo);

    boolean existsByFdStatementFdAccountNoAndFdStatementDate(
            String fdAccountNo,
            LocalDateTime statementDate);
}