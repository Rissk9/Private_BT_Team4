package com.BankingTech.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface FDTransactionService {

    void recordTransaction(
            String fdAccountNo,
            String transactionType,
            BigDecimal amount,
            BigDecimal penaltyAmount,
            LocalDateTime transactionDate,
            String remarks);
}