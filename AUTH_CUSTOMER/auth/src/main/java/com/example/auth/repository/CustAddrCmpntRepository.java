package com.example.auth.repository;

import com.example.auth.entity.CustAddrCmpnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.auth.entity.CustDtl;
import java.util.List;

@Repository
public interface CustAddrCmpntRepository extends JpaRepository<CustAddrCmpnt, Long> {
    List<CustAddrCmpnt> findByCustomer(CustDtl customer);
}
