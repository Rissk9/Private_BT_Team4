package com.BankingTech.demo.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications_log")
@AttributeOverrides({

        @AttributeOverride(name = "crudValue", column = @Column(name = "notification_log_crud_value")),

        @AttributeOverride(name = "userId", column = @Column(name = "notification_log_user_id")),

        @AttributeOverride(name = "wsId", column = @Column(name = "notification_log_ws_id")),

        @AttributeOverride(name = "hostTs", column = @Column(name = "notification_log_host_ts")),

        @AttributeOverride(name = "localTs", column = @Column(name = "notification_log_local_ts")),

        @AttributeOverride(name = "ruleSystemId", column = @Column(name = "notification_log_rule_system_id")),

        @AttributeOverride(name = "acptTs", column = @Column(name = "notification_log_acpt_ts")),

        @AttributeOverride(name = "acptTsUtcOfst", column = @Column(name = "notification_log_acpt_ts_utc_ofst")),

        @AttributeOverride(name = "uuid", column = @Column(name = "notification_log_uuid"))
})
public class NotificationLogEntity extends AuditEntity {

    @Id
    @Column(name = "notification_log_id", nullable = false, unique = true, length = 50)
    private String notificationLogId;

    @Column(name = "notification_log_customer_id", nullable = false, length = 30)
    private String notificationLogCustomerId;

    @Column(name = "notification_log_fd_account_no", nullable = false, length = 30)
    private String notificationLogFdAccountNo;

    @Column(name = "notification_log_channel", nullable = false, length = 20)
    private String notificationLogChannel;

    @Column(name = "notification_log_notification_type", nullable = false, length = 50)
    private String notificationLogNotificationType;

    @Column(name = "notification_log_recipient", nullable = false, length = 255)
    private String notificationLogRecipient;

    @Column(name = "notification_log_subject", length = 255)
    private String notificationLogSubject;

    @Column(name = "notification_log_status", nullable = false, length = 20)
    private String notificationLogStatus;

    @Column(name = "notification_log_sent_at")
    private LocalDateTime notificationLogSentAt;

    @Column(name = "notification_log_error_message", length = 1000)
    private String notificationLogErrorMessage;

    public NotificationLogEntity() {
    }

    public String getNotificationLogId() {
        return notificationLogId;
    }

    public void setNotificationLogId(String notificationLogId) {
        this.notificationLogId = notificationLogId;
    }

    public String getNotificationLogCustomerId() {
        return notificationLogCustomerId;
    }

    public void setNotificationLogCustomerId(String notificationLogCustomerId) {
        this.notificationLogCustomerId = notificationLogCustomerId;
    }

    public String getNotificationLogFdAccountNo() {
        return notificationLogFdAccountNo;
    }

    public void setNotificationLogFdAccountNo(String notificationLogFdAccountNo) {
        this.notificationLogFdAccountNo = notificationLogFdAccountNo;
    }

    public String getNotificationLogChannel() {
        return notificationLogChannel;
    }

    public void setNotificationLogChannel(String notificationLogChannel) {
        this.notificationLogChannel = notificationLogChannel;
    }

    public String getNotificationLogNotificationType() {
        return notificationLogNotificationType;
    }

    public void setNotificationLogNotificationType(
            String notificationLogNotificationType) {
        this.notificationLogNotificationType = notificationLogNotificationType;
    }

    public String getNotificationLogRecipient() {
        return notificationLogRecipient;
    }

    public void setNotificationLogRecipient(String notificationLogRecipient) {
        this.notificationLogRecipient = notificationLogRecipient;
    }

    public String getNotificationLogSubject() {
        return notificationLogSubject;
    }

    public void setNotificationLogSubject(String notificationLogSubject) {
        this.notificationLogSubject = notificationLogSubject;
    }

    public String getNotificationLogStatus() {
        return notificationLogStatus;
    }

    public void setNotificationLogStatus(String notificationLogStatus) {
        this.notificationLogStatus = notificationLogStatus;
    }

    public LocalDateTime getNotificationLogSentAt() {
        return notificationLogSentAt;
    }

    public void setNotificationLogSentAt(LocalDateTime notificationLogSentAt) {
        this.notificationLogSentAt = notificationLogSentAt;
    }

    public String getNotificationLogErrorMessage() {
        return notificationLogErrorMessage;
    }

    public void setNotificationLogErrorMessage(String notificationLogErrorMessage) {
        this.notificationLogErrorMessage = notificationLogErrorMessage;
    }
}