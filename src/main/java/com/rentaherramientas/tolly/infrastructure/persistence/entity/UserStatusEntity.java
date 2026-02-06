package com.rentaherramientas.tolly.infrastructure.persistence.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "user_statuses")
public class UserStatusEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_status_id")
  private Long id;

  @Column(nullable = false)
  private String name;
  // Constructores
  public UserStatusEntity() {
  }



  public UserStatusEntity(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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
