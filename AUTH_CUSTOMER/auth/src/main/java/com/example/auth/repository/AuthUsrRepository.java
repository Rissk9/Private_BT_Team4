package com.example.auth.repository;

import com.example.auth.entity.AuthRole;
import com.example.auth.entity.AuthUsr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthUsrRepository extends JpaRepository<AuthUsr, Long> {

    Optional<AuthUsr> findByEmail(String email);

    Optional<AuthUsr> findByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByUsername(String username);

	Optional<AuthUsr> findByUsername(String username);
}