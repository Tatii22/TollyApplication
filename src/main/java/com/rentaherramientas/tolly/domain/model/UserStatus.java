package com.rentaherramientas.tolly.domain.model;

import java.util.UUID;

public class UserStatus {

  private UUID id;
  private String statusName;

  private UserStatus() {
  }

  public UserStatus(UUID id, String statusName) {
    this.id = id;
    this.statusName = statusName;
  }

  public static UserStatus reconstruct(UUID id, String statusName) {
    UserStatus userStatus = new UserStatus();
    userStatus.id = id;
    userStatus.statusName = statusName;
    return userStatus;
  }

  public static UserStatus create(String statusName) {
    if (statusName == null || statusName.isBlank()) {
      throw new IllegalArgumentException("El nombre del estado de usuario no puede estar vacío");
    }
    UserStatus userStatus = new UserStatus();
    userStatus.id = UUID.randomUUID();
    userStatus.statusName = statusName;
    return userStatus;
  }

  public boolean existsStatus(String statusName) {
    return this.statusName.equalsIgnoreCase(statusName);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    UserStatus that = (UserStatus) o;
    return id != null && id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getStatusName() {
    return statusName;
  }

  public void setStatusName(String statusName) {
    this.statusName = statusName;
  }

  

}
