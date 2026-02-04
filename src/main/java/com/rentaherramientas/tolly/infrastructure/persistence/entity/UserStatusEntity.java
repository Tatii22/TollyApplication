package com.rentaherramientas.tolly.infrastructure.persistence.entity;


import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "user_statuses")
public class UserStatusEntity {

  @Id
  @Column(columnDefinition = "CHAR(36)", length = 36, name = "user_status_id" )
  private UUID id;

  @Column(nullable = false)
  private String name;
  // Constructores
  public UserStatusEntity() {
  }



  public UserStatusEntity(UUID id, String name) {
    this.id = id;
    this.name = name;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  // Getters y Setters


}
