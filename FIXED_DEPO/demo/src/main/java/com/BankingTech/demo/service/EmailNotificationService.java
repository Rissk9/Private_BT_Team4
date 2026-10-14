package com.BankingTech.demo.service;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface EmailNotificationService {

    void sendMaturityNotification(
            String customerId,
            String fdAccountNo,
            String recipientEmail,
            BigDecimal maturityAmount,
            LocalDate maturityDate);
}