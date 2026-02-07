package com.rentaherramientas.tolly.application.dto.invoice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class InvoiceResponse {

  private Long id;
  private String code;
  private Long paymentId;
  private Long reservationId;
  private Long clientId;
  private LocalDateTime issueDate;
  private BigDecimal total;
  private List<InvoiceDetailResponse> details;

  public InvoiceResponse(Long id,
                         String code,
                         Long paymentId,
                         Long reservationId,
                         Long clientId,
                         LocalDateTime issueDate,
                         BigDecimal total,
                         List<InvoiceDetailResponse> details) {
    this.id = id;
    this.code = code;
    this.paymentId = paymentId;
    this.reservationId = reservationId;
    this.clientId = clientId;
    this.issueDate = issueDate;
    this.total = total;
    this.details = details;
  }

  public Long getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public Long getPaymentId() {
    return paymentId;
  }

  public Long getReservationId() {
    return reservationId;
  }

  public Long getClientId() {
    return clientId;
  }

  public LocalDateTime getIssueDate() {
    return issueDate;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public List<InvoiceDetailResponse> getDetails() {
    return details;
  }
}
