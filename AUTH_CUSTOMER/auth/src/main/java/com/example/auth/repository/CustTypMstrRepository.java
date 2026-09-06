package com.example.auth.repository;

import com.example.auth.entity.CustTypMstr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustTypMstrRepository extends JpaRepository<CustTypMstr, Long> {

    Optional<CustTypMstr> findByTypeCode(String typeCode);
}