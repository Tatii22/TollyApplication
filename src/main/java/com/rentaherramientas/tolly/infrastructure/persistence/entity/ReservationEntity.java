package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;


@Entity
@Table(name = "reservations")
public class ReservationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_reservation")
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "client_id", nullable = false)
  private ClientEntity client;

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  @Column(name = "total", nullable = false)
  private BigDecimal totalPrice;
  /** Campo para el estado de la devolución: PENDIENTE_DEVOLUCION, DEVUELTO_OK, DEVUELTO_CON_DAÑOS.
   * El estado inicial debe ser PENDIENTE_DEVOLUCION */
  @Enumerated(EnumType.STRING)
  @Column(name = "estado_devolucion", nullable = false)
  private EstadoDevolucion estadoDevolucion = EstadoDevolucion.PENDIENTE_DEVOLUCION;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "id_reservation_status", nullable = false)
  private ReservationStatusEntity reservationStatus;

  @Column(name = "created_at", nullable = false, length = 50)
  private LocalDate createdAt;

  public ReservationEntity() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ClientEntity getClient() {
    return client;
  }

  public void setClient(ClientEntity client) {
    this.client = client;
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

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  public ReservationStatusEntity getReservationStatus() {
    return reservationStatus;
  }

  public void setReservationStatus(ReservationStatusEntity reservationStatus) {
    this.reservationStatus = reservationStatus;
  }

  public LocalDate getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDate createdAt) {
    this.createdAt = createdAt;
  }

  public EstadoDevolucion getEstadoDevolucion() {
    return estadoDevolucion;
  }

  public void setEstadoDevolucion(EstadoDevolucion estadoDevolucion) {
    this.estadoDevolucion = estadoDevolucion;
  }
}
