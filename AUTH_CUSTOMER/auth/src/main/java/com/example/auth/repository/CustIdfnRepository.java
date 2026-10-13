package com.example.auth.repository;

import com.example.auth.entity.CustIdfn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.auth.entity.CustDtl;
import java.util.List;

@Repository
public interface CustIdfnRepository extends JpaRepository<CustIdfn, Long> {
    List<CustIdfn> findByCustomer(CustDtl customer);
}
