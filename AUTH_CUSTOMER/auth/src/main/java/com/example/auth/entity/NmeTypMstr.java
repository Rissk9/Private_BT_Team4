package com.example.auth.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "nme_typ_mstr")
public class NmeTypMstr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "name_type_id")
    private Long nameTypeId;

    @Column(name = "name_type", length = 50, nullable = false)
    private String nameType;

    @Column(name = "description", length = 255)
    private String description;

    // Getters and setters
	public Long getNameTypeId() {
		return nameTypeId;
	}

	public void setNameTypeId(Long nameTypeId) {
		this.nameTypeId = nameTypeId;
	}

	public String getNameType() {
		return nameType;
	}

	public void setNameType(String nameType) {
		this.nameType = nameType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		
	}
    
    
}