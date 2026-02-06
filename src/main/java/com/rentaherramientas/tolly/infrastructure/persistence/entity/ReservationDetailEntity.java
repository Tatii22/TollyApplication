package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "reservation_detail")
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
  @JoinColumn(name = "id_reservation", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private ReservationEntity reservation;

  /* =========================
     ATRIBUTOS PROPIOS
     ========================= */

  @Column(name = "daily_price", nullable = false, precision = 10, scale = 2)
  private BigDecimal dailyPrice;

  @Column(name = "rental_days", nullable = false)
  private Integer rentalDay;

  @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
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
      BigDecimal dailyPrice,
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
  public BigDecimal getDailyPrice() {
    return dailyPrice;
  }

  public void setDailyPrice(BigDecimal dailyPrice) {
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
