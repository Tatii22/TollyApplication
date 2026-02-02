package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "clients")
public class ClientEntity {

  @Id
  @Column(columnDefinition = "CHAR(36)")
  private UUID id;

  @Column(name = "user_id", nullable = false, unique = true, columnDefinition = "CHAR(36)")
  private UUID userId;

  @Column(nullable = false, length = 255)
  private String address;

  public ClientEntity() {
  }

  public UUID getId() {
    return id;
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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

}
