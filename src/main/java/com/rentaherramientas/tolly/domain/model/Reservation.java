package com.rentaherramientas.tolly.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Reservation {

  private Long id;
  private UUID clientId;
  private LocalDate startDate;
  private LocalDate endDate;
  private BigDecimal total;
  private ReservationStatus status;
  private LocalDate createdAt;

  public Reservation() {
  }

  public static Reservation reconstruct(Long id, UUID clientId, LocalDate startDate,
      LocalDate endDate, BigDecimal total,
      ReservationStatus status, LocalDate createdAt) {
    Reservation reservation = new Reservation();
    reservation.id = id;
    reservation.clientId = clientId;
    reservation.startDate = startDate;
    reservation.endDate = endDate;
    reservation.total = total;
    reservation.status = status;
    reservation.createdAt = createdAt;
    return reservation;
  }

  public static Reservation create(UUID clientId, LocalDate startDate,
      LocalDate endDate, BigDecimal total,
      ReservationStatus status, LocalDate createdAt) {
    Reservation reservation = new Reservation();
    reservation.clientId = clientId;
    reservation.startDate = startDate;
    reservation.endDate = endDate;
    reservation.total = total;
    reservation.status = status;
    reservation.createdAt = createdAt;
    return reservation;
  }

  public static Reservation createEmpty() {
    return new Reservation();
  }

  public static Reservation createWithId(Long id) {
    Reservation reservation = new Reservation();
    reservation.id = id;
    return reservation;
  }

  public boolean searchId(Long id) {
    return this.id.equals(id);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UUID getClientId() {
    return clientId;
  }

  public void setClientId(UUID clientId) {
    this.clientId = clientId;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public ReservationStatus getStatus() {
    return status;
  }

  public void setStatus(ReservationStatus status) {
    this.status = status;
  }

  public LocalDate getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDate createdAt) {
    this.createdAt = createdAt;
  }





}
