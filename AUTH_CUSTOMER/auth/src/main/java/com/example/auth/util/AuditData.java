package com.example.auth.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AuditData {

    private LocalDate effectiveDate;
    private String crudValue;
    private String userId;
    private String workstationId;
    private String programId;
    private LocalDateTime hostTimestamp;
    private LocalDateTime localTimestamp;
    private LocalDateTime acceptanceTimestamp;
    private LocalDateTime acceptanceTimestampUtcOffset;
    private String uuid;

    public AuditData() {
    }

    public AuditData(
            LocalDate effectiveDate,
            String crudValue,
            String userId,
            String workstationId,
            String programId,
            LocalDateTime hostTimestamp,
            LocalDateTime localTimestamp,
            LocalDateTime acceptanceTimestamp,
            LocalDateTime acceptanceTimestampUtcOffset,
            String uuid) {

        this.effectiveDate = effectiveDate;
        this.crudValue = crudValue;
        this.userId = userId;
        this.workstationId = workstationId;
        this.programId = programId;
        this.hostTimestamp = hostTimestamp;
        this.localTimestamp = localTimestamp;
        this.acceptanceTimestamp = acceptanceTimestamp;
        this.acceptanceTimestampUtcOffset = acceptanceTimestampUtcOffset;
        this.uuid = uuid;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getCrudValue() {
        return crudValue;
    }

    public void setCrudValue(String crudValue) {
        this.crudValue = crudValue;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWorkstationId() {
        return workstationId;
    }

    public void setWorkstationId(String workstationId) {
        this.workstationId = workstationId;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public LocalDateTime getHostTimestamp() {
        return hostTimestamp;
    }

    public void setHostTimestamp(LocalDateTime hostTimestamp) {
        this.hostTimestamp = hostTimestamp;
    }

    public LocalDateTime getLocalTimestamp() {
        return localTimestamp;
    }

    public void setLocalTimestamp(LocalDateTime localTimestamp) {
        this.localTimestamp = localTimestamp;
    }

    public LocalDateTime getAcceptanceTimestamp() {
        return acceptanceTimestamp;
    }

    public void setAcceptanceTimestamp(LocalDateTime acceptanceTimestamp) {
        this.acceptanceTimestamp = acceptanceTimestamp;
    }

    public LocalDateTime getAcceptanceTimestampUtcOffset() {
        return acceptanceTimestampUtcOffset;
    }

    public void setAcceptanceTimestampUtcOffset(
            LocalDateTime acceptanceTimestampUtcOffset) {
        this.acceptanceTimestampUtcOffset = acceptanceTimestampUtcOffset;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}