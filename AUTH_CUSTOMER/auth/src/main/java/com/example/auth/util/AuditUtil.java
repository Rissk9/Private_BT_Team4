package com.example.auth.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class AuditUtil {

    public AuditData createAudit(String programId) {

        LocalDateTime now = LocalDateTime.now();

        return new AuditData(
                LocalDate.now(),
                "C",
                "SYSTEM",
                "AUTH_SERVICE",
                programId,
                now,
                now,
                now,
                now,
                UUID.randomUUID().toString()
        );
    }

    public AuditData updateAudit(String programId) {

        LocalDateTime now = LocalDateTime.now();

        return new AuditData(
                LocalDate.now(),
                "U",
                "SYSTEM",
                "AUTH_SERVICE",
                programId,
                now,
                now,
                now,
                now,
                UUID.randomUUID().toString()
        );
    }
}