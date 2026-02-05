package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "payments")
public class PaymentEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "reservation_id", nullable = false)
  private ReservationEntity reservationId;

  @Column(name = "amount", nullable = false, precision = 10, scale = 2)
  private BigDecimal amount;

  @Column(name = "payment_date", nullable = false)
  private LocalDateTime paymentDate;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "status_id", nullable = false)
  private PaymentStatusEntity status;

  // Constructor vacío requerido por JPA
  public PaymentEntity() {
  }

  // Constructor sin ID
  public PaymentEntity(ReservationEntity reservationId,
      BigDecimal amount,
      LocalDateTime paymentDate,
      PaymentStatusEntity status) {
    this.reservationId = reservationId;
    this.amount = amount;
    this.paymentDate = paymentDate;
    this.status = status;
  }

  // Constructor completo
  public PaymentEntity(Long id,
      ReservationEntity reservationId,
      BigDecimal amount,
      LocalDateTime paymentDate,
      PaymentStatusEntity status) {
    this.id = id;
    this.reservationId = reservationId;
    this.amount = amount;
    this.paymentDate = paymentDate;
    this.status = status;
  }

  // Getters y Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ReservationEntity getReservationId() {
    return reservationId;
  }

  public void setReservationId(ReservationEntity reservationId) {
    this.reservationId = reservationId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public LocalDateTime getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(LocalDateTime paymentDate) {
    this.paymentDate = paymentDate;
  }

  public PaymentStatusEntity getStatus() {
    return status;
  }

  public void setStatus(PaymentStatusEntity status) {
    this.status = status;
  }
}
