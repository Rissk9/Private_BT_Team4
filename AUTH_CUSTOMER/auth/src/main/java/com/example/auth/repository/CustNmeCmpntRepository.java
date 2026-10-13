package com.example.auth.repository;

import com.example.auth.entity.CustNmeCmpnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.auth.entity.CustDtl;
import java.util.List;

@Repository
public interface CustNmeCmpntRepository extends JpaRepository<CustNmeCmpnt, Long> {
    List<CustNmeCmpnt> findByCustomer(CustDtl customer);
}
