package com.BankingTech.demo.repository;

import com.BankingTech.demo.entity.FdCustomerRegistry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FdCustomerRegistryRepository extends JpaRepository<FdCustomerRegistry, String> {
}