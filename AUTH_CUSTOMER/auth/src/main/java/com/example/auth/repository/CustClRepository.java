package com.example.auth.repository;

import com.example.auth.entity.CustCl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustClRepository extends JpaRepository<CustCl, Long> {

    Optional<CustCl> findByClassificationTypeAndClassificationTypeValue(String classificationType,String classificationTypeValue);
}