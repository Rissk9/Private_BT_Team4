package com.example.auth.repository;

import com.example.auth.entity.NmeTypMstr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NmeTypMstrRepository extends JpaRepository<NmeTypMstr, Long> {
    Optional<NmeTypMstr> findByNameType(String nameType);
}
