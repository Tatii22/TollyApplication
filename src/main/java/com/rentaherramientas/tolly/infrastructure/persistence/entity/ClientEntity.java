package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "clients")
public class ClientEntity {

  @Id
  @Column(columnDefinition = "CHAR(36)", nullable = false, unique = true, name = "id")
  private UUID id;

  @OneToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false, unique = true, columnDefinition = "CHAR(36)")
  private UserEntity userId;


  @Column(nullable = false, length = 100, name = "first_name")
  private String firstName;

  @Column(nullable = false, length = 100, name = "last_name")
  private String lastName;

  @Column(nullable = false, length = 15, name = "national_id")
  private String nationalId;

  @Column(nullable = false, length = 255, name = "address")
  private String address;

  @Column(nullable = false, length = 15, name = "phone_number")
  private String phoneNumber;

  public ClientEntity() {
  }

  public UUID getId() {
    return id;
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

  public String getNationalId() {
    return nationalId;
  }

  public void setNationalId(String nationalId) {
    this.nationalId = nationalId;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }



}
