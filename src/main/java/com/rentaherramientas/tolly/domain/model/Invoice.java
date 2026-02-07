package com.rentaherramientas.tolly.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Invoice {

  private final Long id;
  private final Reservation reservation;
  private final Payment payment;
  private final LocalDateTime issueDate;
  private final BigDecimal total;
  private final List<InvoiceDetail> details;

  public Invoice(Long id,
                 Reservation reservation,
                 Payment payment,
                 LocalDateTime issueDate,
                 BigDecimal total,
                 List<InvoiceDetail> details) {
    if (reservation == null) {
      throw new IllegalArgumentException("Reservation is required");
    }
    if (payment == null) {
      throw new IllegalArgumentException("Payment is required");
    }
    if (issueDate == null) {
      throw new IllegalArgumentException("Issue date is required");
    }
    if (total == null) {
      throw new IllegalArgumentException("Total is required");
    }
    if (details == null || details.isEmpty()) {
      throw new IllegalArgumentException("Invoice details are required");
    }
    this.id = id;
    this.reservation = reservation;
    this.payment = payment;
    this.issueDate = issueDate;
    this.total = total;
    this.details = List.copyOf(details);
  }

  public Long getId() {
    return id;
  }

  public Reservation getReservation() {
    return reservation;
  }

  public Payment getPayment() {
    return payment;
  }

  public LocalDateTime getIssueDate() {
    return issueDate;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public List<InvoiceDetail> getDetails() {
    return details;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Invoice)) return false;
    Invoice invoice = (Invoice) o;
    return Objects.equals(id, invoice.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
