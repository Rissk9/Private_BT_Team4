package com.example.auth.repository;

import com.example.auth.entity.AddrTypMstr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddrTypMstrRepository extends JpaRepository<AddrTypMstr, Long> {
    Optional<AddrTypMstr> findByAddressType(String addressType);
}
