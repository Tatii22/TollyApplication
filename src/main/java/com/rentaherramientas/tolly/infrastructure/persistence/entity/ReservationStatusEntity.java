package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "reservation_status")
public class ReservationStatusEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_reservation_status", nullable = false)
  private Long id;

  @Column(name = "status_name", nullable = false, length = 50)
  private String statusName;

  public ReservationStatusEntity() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStatusName() {
    return statusName;
  }

  public void setStatusName(String statusName) {
    this.statusName = statusName;
  }


}
