package com.example.auth.service;

import com.example.auth.entity.AuthUsr;
import com.example.auth.entity.LoginAudit;
import com.example.auth.repository.LoginAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class LoginAuditService {

    @Autowired
    private LoginAuditRepository loginAuditRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordAudit(AuthUsr user, String status, String failureReason, String ipAddress, String userAgent) {
        LoginAudit audit = new LoginAudit();
        audit.setUser(user);
        audit.setLoginStatus(status);
        audit.setFailureReason(failureReason);
        audit.setIpAddress(ipAddress);
        audit.setUserAgent(userAgent);
        audit.setLoginTimestamp(LocalDateTime.now());
        audit.setCreatedAt(LocalDateTime.now());
        loginAuditRepository.save(audit);
    }

    @Transactional
    public void updateLogoutTimestamp(AuthUsr user) {
        loginAuditRepository.findTopByUserAndLoginStatusOrderByLoginTimestampDesc(user, "SUCCESS")
                .ifPresent(audit -> {
                    audit.setLogoutTimestamp(LocalDateTime.now());
                    loginAuditRepository.save(audit);
                });
    }
}
