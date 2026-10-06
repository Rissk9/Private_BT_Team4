package com.example.auth.repository;

import com.example.auth.entity.CustNmeCmpnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustNmeCmpntRepository extends JpaRepository<CustNmeCmpnt, Long> {
}
