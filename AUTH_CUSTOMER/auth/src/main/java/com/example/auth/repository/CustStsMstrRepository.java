package com.example.auth.repository;

import com.example.auth.entity.CustStsMstr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustStsMstrRepository extends JpaRepository<CustStsMstr, Long> {

    Optional<CustStsMstr> findByStatusCode(String statusCode);
}