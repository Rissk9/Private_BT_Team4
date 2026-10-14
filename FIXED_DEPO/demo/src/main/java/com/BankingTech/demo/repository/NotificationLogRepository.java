package com.BankingTech.demo.repository;

import com.BankingTech.demo.entity.NotificationLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationLogRepository
        extends JpaRepository<NotificationLogEntity, String> {

    List<NotificationLogEntity> findByNotificationLogFdAccountNo(
            String fdAccountNo);

    List<NotificationLogEntity> findByNotificationLogCustomerId(
            String customerId);
}