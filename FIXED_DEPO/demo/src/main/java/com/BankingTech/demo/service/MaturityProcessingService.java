package com.BankingTech.demo.service;

import java.time.LocalDate;

public interface MaturityProcessingService {

    void processMaturedAccounts(LocalDate businessDate);
}