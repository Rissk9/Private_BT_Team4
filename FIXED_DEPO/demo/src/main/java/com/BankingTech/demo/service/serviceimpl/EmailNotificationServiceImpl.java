package com.BankingTech.demo.service.serviceimpl;

import com.BankingTech.demo.entity.NotificationLogEntity;
import com.BankingTech.demo.repository.NotificationLogRepository;
import com.BankingTech.demo.service.EmailNotificationService;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class EmailNotificationServiceImpl
        implements EmailNotificationService {

    private final JavaMailSender mailSender;
    private final NotificationLogRepository notificationLogRepository;

    public EmailNotificationServiceImpl(
            JavaMailSender mailSender,
            NotificationLogRepository notificationLogRepository) {

        this.mailSender = mailSender;
        this.notificationLogRepository = notificationLogRepository;
    }

    @Override
    public void sendMaturityNotification(
            String customerId,
            String fdAccountNo,
            String recipientEmail,
            BigDecimal maturityAmount,
            LocalDate maturityDate) {

        String subject = "Your Fixed Deposit Has Matured Successfully";

        String message = "Dear Customer,\n\n"
                + "Your Fixed Deposit has matured successfully.\n\n"
                + "FD Account Number: " + fdAccountNo + "\n"
                + "Maturity Date: " + maturityDate + "\n"
                + "Maturity Amount: ₹"
                + maturityAmount
                + "\n\n"
                + "Thank you for banking with us.";

        NotificationLogEntity log = new NotificationLogEntity();

        log.setNotificationLogId(
                "NTF-" + UUID.randomUUID());

        log.setNotificationLogCustomerId(customerId);
        log.setNotificationLogFdAccountNo(fdAccountNo);
        log.setNotificationLogChannel("EMAIL");
        log.setNotificationLogNotificationType("MATURITY_NOTICE");
        log.setNotificationLogRecipient(recipientEmail);
        log.setNotificationLogSubject(subject);

        try {

            SimpleMailMessage mail = new SimpleMailMessage();

            mail.setTo(recipientEmail);
            mail.setSubject(subject);
            mail.setText(message);

            mailSender.send(mail);

            log.setNotificationLogStatus("SENT");
            log.setNotificationLogSentAt(LocalDateTime.now());

        } catch (Exception e) {

            log.setNotificationLogStatus("FAILED");
            log.setNotificationLogErrorMessage(
                    e.getMessage());

            notificationLogRepository.save(log);

            throw new RuntimeException(
                    "Failed to send maturity notification email",
                    e);
        }

        notificationLogRepository.save(log);
    }
}