package com.example.auth.repository;

import com.example.auth.entity.CustCntctDtls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.auth.entity.CustDtl;
import java.util.List;

@Repository
public interface CustCntctDtlsRepository extends JpaRepository<CustCntctDtls, Long> {
    List<CustCntctDtls> findByCustomer(CustDtl customer);
}
