package com.example.auth.repository;

import com.example.auth.entity.IdfnTypMstr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdfnTypMstrRepository extends JpaRepository<IdfnTypMstr, Long> {
    Optional<IdfnTypMstr> findByIdentificationType(String identificationType);
}
