package com.example.auth.repository;

import com.example.auth.entity.LoginAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.auth.entity.AuthUsr;
import java.util.Optional;

@Repository
public interface LoginAuditRepository extends JpaRepository<LoginAudit, Long> {
    Optional<LoginAudit> findTopByUserAndLoginStatusOrderByLoginTimestampDesc(AuthUsr user, String loginStatus);
}
