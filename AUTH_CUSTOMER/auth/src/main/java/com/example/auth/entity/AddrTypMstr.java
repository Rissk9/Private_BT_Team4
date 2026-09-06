package com.example.auth.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "addr_typ_mstr")
public class AddrTypMstr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_type_id")
    private Long addressTypeId;

    @Column(name = "address_type", length = 50, nullable = false)
    private String addressType;

    @Column(name = "description", length = 255)
    private String description;

    public Long getAddressTypeId() {
        return addressTypeId;
    }

    public void setAddressTypeId(Long addressTypeId) {
        this.addressTypeId = addressTypeId;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}