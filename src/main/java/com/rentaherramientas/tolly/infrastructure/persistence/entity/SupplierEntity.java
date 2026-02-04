package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "suppliers")
public class SupplierEntity {

    @Id
    @Column(columnDefinition = "CHAR(36)", name = "id", nullable = false, unique = true)
    private UUID id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true, columnDefinition = "CHAR(36)")
    private UserEntity userId;

    @Column(nullable = false, length = 10, name = "phone")
    private String phone;

    @Column(nullable = false, length = 20, name = "company_name")
    private String companyName;

    @Column(nullable = false, length = 15, name = "identification")
    private String identification;

    @Column(nullable = false, length = 100, name = "contact_name")
    private String contactName;


    public SupplierEntity() {}

    public UUID getId() {
      return id;
    }

    public SupplierEntity(UUID id, UserEntity userId, String phone, String company, String identification, String contactName) {
      this.id = id;
      this.userId = userId;
      this.phone = phone;
      this.companyName = company;
      this.identification = identification;
      this.contactName = contactName;
    }

    public void setId(UUID id) {
      this.id = id;
    }

    public UserEntity getUserId() {
      return userId;
    }

    public void setUserId(UserEntity userId) {
      this.userId = userId;
    }

    public String getIdentification() {
      return identification;
    }

    public void setIdentification(String identification) {
      this.identification = identification;
    }

    public String getContactName() {
      return contactName;
    }

    public void setContactName(String contactName) {
      this.contactName = contactName;
    }

    public String getPhone() {
      return phone;
    }

    public void setPhone(String phone) {
      this.phone = phone;
    }

    public String getCompanyName() {
      return companyName;
    }

    public void setCompanyName(String companyName) {
      this.companyName = companyName;
    }


}
