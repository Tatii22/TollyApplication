package com.rentaherramientas.tolly.domain.model;

public class ReservationStatus {
  private Long id_reservacion;
  private String name;

  public ReservationStatus(Long id, String name) {
    this.id_reservacion = id;
    this.name = name;
  }

  public ReservationStatus() {
  }

  public static ReservationStatus reconstruct(Long id, String name) {
    ReservationStatus reservationStatus = new ReservationStatus();
    reservationStatus.id_reservacion = id;
    reservationStatus.name = name;
    return reservationStatus;
  }

  public static ReservationStatus create(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("El nombre del estado de la reserva no puede estar vacío");
    }
    ReservationStatus reservationStatus = new ReservationStatus();
    reservationStatus.id_reservacion = null;
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
    return id_reservacion != null && id_reservacion.equals(that.id_reservacion);
  }

  @Override
  public int hashCode() {
    return id_reservacion != null ? id_reservacion.hashCode() : 0;
  }

  public Long getId_reservacion() {
    return id_reservacion;
  }

  public void setId_reservacion(Long id) {
    this.id_reservacion = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  

}
