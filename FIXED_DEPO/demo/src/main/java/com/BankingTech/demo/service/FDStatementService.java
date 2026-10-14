package com.BankingTech.demo.service;

import java.time.LocalDate;

public interface FDStatementService {

    void generateStatement(String fdAccountNo);

    void generateStatement(
            String fdAccountNo,
            LocalDate businessDate);
}