package com.example.auth.repository;

import com.example.auth.entity.CntctTypMstr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CntctTypMstrRepository extends JpaRepository<CntctTypMstr, Long> {
    Optional<CntctTypMstr> findByContactType(String contactType);
}
