package com.example.auth.repository;

import com.example.auth.entity.CustCntctDtls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustCntctDtlsRepository extends JpaRepository<CustCntctDtls, Long> {
}
