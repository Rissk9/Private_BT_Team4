package com.example.auth.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cntct_typ_mstr")
public class CntctTypMstr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_type_id")
    private Long contactTypeId;

    @Column(name = "contact_type", length = 50, nullable = false)
    private String contactType;

    @Column(name = "description", length = 255)
    private String description;
    
    // Getters and setters

	public Long getContactTypeId() {
		return contactTypeId;
	}

	public void setContactTypeId(Long contactTypeId) {
		this.contactTypeId = contactTypeId;
	}

	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    
    
    
}