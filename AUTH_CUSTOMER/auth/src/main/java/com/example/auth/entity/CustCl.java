package com.example.auth.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "cust_cl")
public class CustCl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CSTCL_ID")
    private Long classificationId;

    @Column(name = "CSTCL_TYP", length = 100, nullable = false)
    private String classificationType;

    @Column(name = "CSTCL_TYP_VALUE", length = 100, nullable = false)
    private String classificationTypeValue;

    @Column(name = "CSTCL_CRUD_VALUE", length = 1)
    private String crudValue;

    @Column(name = "CSTCL_USER_ID", length = 100)
    private String userId;

    @Column(name = "CSTCL_WS_ID", length = 100)
    private String workstationId;

    @Column(name = "CSTCL_PRGM_ID", length = 100)
    private String programId;

    @Column(name = "CSTCL_HOST_TS")
    private LocalDateTime hostTimestamp;

    @Column(name = "CSTCL_LOCAL_TS")
    private LocalDateTime localTimestamp;

    @Column(name = "CSTCL_ACPT_TS")
    private LocalDateTime acceptanceTimestamp;

    @Column(name = "CSTCL_ACPT_TS_UTC_OFST")
    private LocalDateTime acceptanceTimestampUtcOffset;

    @Column(name = "CSTCL_UUID", length = 100)
    private String uuid;

    // Getters and Setters

    public Long getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(Long classificationId) {
        this.classificationId = classificationId;
    }

    public String getClassificationType() {
        return classificationType;
    }

    public void setClassificationType(String classificationType) {
        this.classificationType = classificationType;
    }

    public String getClassificationTypeValue() {
        return classificationTypeValue;
    }

    public void setClassificationTypeValue(String classificationTypeValue) {
        this.classificationTypeValue = classificationTypeValue;
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

    public void setAcceptanceTimestampUtcOffset(LocalDateTime acceptanceTimestampUtcOffset) {
        this.acceptanceTimestampUtcOffset = acceptanceTimestampUtcOffset;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}