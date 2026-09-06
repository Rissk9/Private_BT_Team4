package com.example.auth.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "idfn_typ_mstr")
public class IdfnTypMstr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identification_type_id")
    private Long identificationTypeId;

    @Column(name = "identification_type", length = 50, nullable = false)
    private String identificationType;

    @Column(name = "description", length = 255)
    private String description;

    // Getters and setters
	public Long getIdentificationTypeId() {
		return identificationTypeId;
	}

	public void setIdentificationTypeId(Long identificationTypeId) {
		this.identificationTypeId = identificationTypeId;
	}

	public String getIdentificationType() {
		return identificationType;
	}

	public void setIdentificationType(String identificationType) {
		this.identificationType = identificationType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    
    
}