package com.example.auth.repository;

import com.example.auth.entity.CustDtl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustDtlRepository extends JpaRepository<CustDtl, Long> {
}