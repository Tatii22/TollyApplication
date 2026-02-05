package com.rentaherramientas.tolly.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Payment {

  private Long id;
  private Long reservationId;
  private BigDecimal amount;
  private LocalDateTime paymentDate;
  private PaymentStatus status;

  // Constructor vacío
  public Payment() {
  }

  // Constructor para CREATE
  private Payment(Long reservationId,
      BigDecimal amount,
      LocalDateTime paymentDate,
      PaymentStatus status) {

    this.reservationId = reservationId;
    this.amount = amount;
    this.paymentDate = paymentDate;
    this.status = status;
    validate();
  }

  // Constructor para RECONSTRUCT
  private Payment(Long id,
      Long reservationId,
      BigDecimal amount,
      LocalDateTime paymentDate,
      PaymentStatus status) {

    this.id = id;
    this.reservationId = reservationId;
    this.amount = amount;
    this.paymentDate = paymentDate;
    this.status = status;
    validate();
  }

  // CREATE
  public static Payment create(Long reservationId,
      BigDecimal amount,
      LocalDateTime paymentDate,
      PaymentStatus status) {

    return new Payment(reservationId, amount, paymentDate, status);
  }

  // RECONSTRUCT
  public static Payment reconstruct(Long id,
      Long reservationId,
      BigDecimal amount,
      LocalDateTime paymentDate,
      PaymentStatus status) {

    if (id == null) {
      throw new IllegalArgumentException("Id is required for reconstruction");
    }
    return new Payment(id, reservationId, amount, paymentDate, status);
  }

  // Validaciones de dominio
  private void validate() {
    if (reservationId == null) {
      throw new IllegalArgumentException("Reservation ID is required");
    }
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Payment amount must be greater than zero");
    }
    if (paymentDate == null) {
      throw new IllegalArgumentException("Payment date is required");
    }
    if (status == null) {
      throw new IllegalArgumentException("Payment status is required");
    }
  }

  // Indica si ya existe en persistencia
  public boolean exists() {
    return this.id != null;
  }

  // Getters y Setters
  public Long getId() {
    return id;
  }

  public Long getReservationId() {
    return reservationId;
  }

  public void setReservationId(Long reservationId) {
    this.reservationId = reservationId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
    validate();
  }

  public LocalDateTime getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(LocalDateTime paymentDate) {
    this.paymentDate = paymentDate;
    validate();
  }

  public PaymentStatus getStatus() {
    return status;
  }

  public void setStatus(PaymentStatus status) {
    this.status = status;
    validate();
  }

  // equals y hashCode por ID
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Payment))
      return false;
    Payment payment = (Payment) o;
    return Objects.equals(id, payment.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
