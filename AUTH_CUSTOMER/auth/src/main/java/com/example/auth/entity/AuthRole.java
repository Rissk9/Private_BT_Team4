package com.example.auth.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "auth_role")
public class AuthRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "role_code", length = 30, nullable = false)
    private String roleCode;

    @Column(name = "role_name", length = 100, nullable = false)
    private String roleName;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "is_active")
    private Boolean active;

    @Column(name = "AUTHROLE_EFCTV_DT")
    private LocalDate effectiveDate;

    @Column(name = "AUTHROLE_CRUD_VALUE", length = 1)
    private String crudValue;

    @Column(name = "AUTHROLE_USER_ID", length = 100)
    private String userId;

    @Column(name = "AUTHROLE_WS_ID", length = 100)
    private String workstationId;

    @Column(name = "AUTHROLE_PRGM_ID", length = 100)
    private String programId;

    @Column(name = "AUTHROLE_HOST_TS")
    private LocalDateTime hostTimestamp;

    @Column(name = "AUTHROLE_LOCAL_TS")
    private LocalDateTime localTimestamp;

    @Column(name = "AUTHROLE_ACPT_TS")
    private LocalDateTime acceptanceTimestamp;

    @Column(name = "AUTHROLE_ACPT_TS_UTC_OFS")
    private LocalDateTime acceptanceTimestampUtcOffset;

    @Column(name = "AUTHROLE_UUID", length = 100)
    private String uuid;

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}