package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "suppliers")
public class SupplierEntity {

    @Id
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true, columnDefinition = "CHAR(36)")
    private UUID userId;

    @Column(nullable = false, length = 10)
    private String phone;

    @Column(nullable = false, length = 20)
    private String company;

    public SupplierEntity() {}

    public UUID getId() {
      return id;
    }

    public SupplierEntity(UUID id, UUID userId, String phone, String company) {
      this.id = id;
      this.userId = userId;
      this.phone = phone;
      this.company = company;
    }

    public void setId(UUID id) {
      this.id = id;
    }

    public UUID getUserId() {
      return userId;
    }

    public void setUserId(UUID userId) {
      this.userId = userId;
    }

    public String getPhone() {
      return phone;
    }

    public void setPhone(String phone) {
      this.phone = phone;
    }

    public String getCompany() {
      return company;
    }

    public void setCompany(String company) {
      this.company = company;
    }




}
