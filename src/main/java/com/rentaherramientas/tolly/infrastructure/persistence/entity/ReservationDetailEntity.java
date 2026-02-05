package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "reservation_details")
public class ReservationDetailEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_reservation_detail")
  private Long id;

  /* =========================
     RELACIONES
     ========================= */

  // Muchas filas del detalle pueden apuntar a una herramienta
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "id_tool", nullable = false)
  private ToolEntity tool;

  // Muchas filas del detalle pueden apuntar a una reserva
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "reservation_id", nullable = false)
  private ReservationEntity reservation;

  /* =========================
     ATRIBUTOS PROPIOS
     ========================= */

  @Column(name = "daily_price", nullable = false)
  private Double dailyPrice;

  @Column(name = "rental_day", nullable = false)
  private Integer rentalDay;

  @Column(name = "sub_total", nullable = false, precision = 38, scale = 2)
  private BigDecimal subTotal;

  /* =========================
     CONSTRUCTORES
     ========================= */

  public ReservationDetailEntity() {
  }

  public ReservationDetailEntity(
      Long id,
      Long toolId,
      Long reservationId,
      Double dailyPrice,
      Integer rentalDay,
      BigDecimal subTotal
  ) {
    this.id = id;
    setToolId(toolId);
    setReservationId(reservationId);
    this.dailyPrice = dailyPrice;
    this.rentalDay = rentalDay;
    this.subTotal = subTotal;
  }

  /* =========================
     GETTERS Y SETTERS
     ========================= */

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  // -------- TOOL --------
  public ToolEntity getTool() {
    return tool;
  }

  public Long getToolId() {
    return tool != null ? tool.getId() : null;
  }

  public void setToolId(Long toolId) {
    if (tool == null) tool = new ToolEntity();
    tool.setId(toolId);
  }

  // ------ RESERVATION ------
  public ReservationEntity getReservation() {
    return reservation;
  }

  public Long getReservationId() {
    return reservation != null ? reservation.getId() : null;
  }

  public void setReservationId(Long reservationId) {
    if (reservation == null) reservation = new ReservationEntity();
    reservation.setId(reservationId);
  }

  // -------- DATA --------
  public Double getDailyPrice() {
    return dailyPrice;
  }

  public void setDailyPrice(Double dailyPrice) {
    this.dailyPrice = dailyPrice;
  }

  public Integer getRentalDay() {
    return rentalDay;
  }

  public void setRentalDay(Integer rentalDay) {
    this.rentalDay = rentalDay;
  }

  public BigDecimal getSubTotal() {
    return subTotal;
  }

  public void setSubTotal(BigDecimal subTotal) {
    this.subTotal = subTotal;
  }
}
