package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import java.util.UUID;

import jakarta.persistence.*;


@Entity
@Table(name = "reservation_status")
public class ReservationStatusEntity {

  @Id
  @Column(name = "id_reservation_status", columnDefinition = "CHAR(36)", nullable = false)
  private UUID id;

  @Column(name = "status_name", nullable = false, length = 50)
  private String statusName;

  public ReservationStatusEntity() {
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
