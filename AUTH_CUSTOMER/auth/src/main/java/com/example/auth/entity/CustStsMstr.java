package com.example.auth.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cust_sts_mstr")
public class CustStsMstr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long statusId;

    @Column(name = "status_code", length = 30, nullable = false)
    private String statusCode;

    @Column(name = "status_name", length = 100, nullable = false)
    private String statusName;

    @Column(name = "description", length = 255)
    private String description;

    // Getters and setters
	public Long getStatusId() {
		return statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    
    
    
}