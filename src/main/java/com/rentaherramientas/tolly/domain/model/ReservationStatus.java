package com.rentaherramientas.tolly.domain.model;

import java.util.UUID;

public class ReservationStatus {
  private UUID id;
  private String name;

  public ReservationStatus(UUID id, String name) {
    this.id = id;
    this.name = name;
  }

  public ReservationStatus() {
  }

  public static ReservationStatus reconstruct(UUID id, String name) {
    ReservationStatus reservationStatus = new ReservationStatus();
    reservationStatus.id = id;
    reservationStatus.name = name;
    return reservationStatus;
  }

  public static ReservationStatus create(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("El nombre del estado de la reserva no puede estar vacío");
    }
    ReservationStatus reservationStatus = new ReservationStatus();
    reservationStatus.id = UUID.randomUUID();
    reservationStatus.name = name;
    return reservationStatus;
  }

  public boolean existsStatus(String name) {
    return this.name.equalsIgnoreCase(name);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    ReservationStatus that = (ReservationStatus) o;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  

}
