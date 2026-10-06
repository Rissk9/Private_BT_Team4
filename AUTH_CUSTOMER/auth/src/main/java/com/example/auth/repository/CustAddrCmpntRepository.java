package com.example.auth.repository;

import com.example.auth.entity.CustAddrCmpnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustAddrCmpntRepository extends JpaRepository<CustAddrCmpnt, Long> {
}
