package com.BankingTech.demo.service;

public interface InterestCalculationService {

    /*
     * Runs interest calculation for all ACTIVE FD accounts.
     */
    void calculateInterestForActiveAccounts();

    /*
     * Calculates interest for one specific FD account.
     *
     * Useful for testing and manual processing.
     */
    void calculateInterestForAccount(
            String fdAccountNo);
}