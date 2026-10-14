package com.BankingTech.demo.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fd_statements")
@AttributeOverrides({
                @AttributeOverride(name = "crudValue", column = @Column(name = "fd_statement_crud_value")),
                @AttributeOverride(name = "userId", column = @Column(name = "fd_statement_user_id")),
                @AttributeOverride(name = "wsId", column = @Column(name = "fd_statement_ws_id")),
                @AttributeOverride(name = "hostTs", column = @Column(name = "fd_statement_host_ts")),
                @AttributeOverride(name = "localTs", column = @Column(name = "fd_statement_local_ts")),
                @AttributeOverride(name = "ruleSystemId", column = @Column(name = "fd_statement_rule_system_id")),
                @AttributeOverride(name = "acptTs", column = @Column(name = "fd_statement_acpt_ts")),
                @AttributeOverride(name = "acptTsUtcOfst", column = @Column(name = "fd_statement_acpt_ts_utc_ofst")),
                @AttributeOverride(name = "uuid", column = @Column(name = "fd_statement_uuid"))
})
public class FDStatementEntity extends AuditEntity {

        @Id
        @Column(name = "fd_statement_id", nullable = false, unique = true, length = 50)
        private String fdStatementId;

        @Column(name = "fd_statement_fd_account_no", nullable = false, length = 30)
        private String fdStatementFdAccountNo;

        @Column(name = "fd_statement_date", nullable = false)
        private LocalDateTime fdStatementDate;

        @Column(name = "fd_statement_principal", nullable = false, precision = 19, scale = 2)
        private BigDecimal fdStatementPrincipal;

        @Column(name = "fd_statement_accrued_interest", nullable = false, precision = 19, scale = 2)
        private BigDecimal fdStatementAccruedInterest;

        public String getFdStatementId() {
                return fdStatementId;
        }

        public void setFdStatementId(String fdStatementId) {
                this.fdStatementId = fdStatementId;
        }

        public String getFdStatementFdAccountNo() {
                return fdStatementFdAccountNo;
        }

        public void setFdStatementFdAccountNo(String fdStatementFdAccountNo) {
                this.fdStatementFdAccountNo = fdStatementFdAccountNo;
        }

        public LocalDateTime getFdStatementDate() {
                return fdStatementDate;
        }

        public void setFdStatementDate(LocalDateTime fdStatementDate) {
                this.fdStatementDate = fdStatementDate;
        }

        public BigDecimal getFdStatementPrincipal() {
                return fdStatementPrincipal;
        }

        public void setFdStatementPrincipal(BigDecimal fdStatementPrincipal) {
                this.fdStatementPrincipal = fdStatementPrincipal;
        }

        public BigDecimal getFdStatementAccruedInterest() {
                return fdStatementAccruedInterest;
        }

        public void setFdStatementAccruedInterest(BigDecimal fdStatementAccruedInterest) {
                this.fdStatementAccruedInterest = fdStatementAccruedInterest;
        }
}