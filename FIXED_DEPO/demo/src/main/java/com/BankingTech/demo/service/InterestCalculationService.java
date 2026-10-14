package com.BankingTech.demo.service;

import java.time.LocalDate;

public interface InterestCalculationService {

    void calculateInterestForActiveAccounts();

    void calculateInterestForAccount(String fdAccountNo);

    void calculateInterestForActiveAccounts(LocalDate businessDate);

    void calculateInterestForAccount(
            String fdAccountNo,
            LocalDate businessDate);
}