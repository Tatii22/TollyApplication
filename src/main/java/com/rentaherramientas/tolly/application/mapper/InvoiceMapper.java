package com.rentaherramientas.tolly.application.mapper;

import java.util.List;

import com.rentaherramientas.tolly.application.dto.invoice.InvoiceDetailResponse;
import com.rentaherramientas.tolly.application.dto.invoice.InvoiceResponse;
import com.rentaherramientas.tolly.domain.model.Invoice;
import com.rentaherramientas.tolly.domain.model.InvoiceDetail;
import com.rentaherramientas.tolly.domain.model.Payment;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.InvoiceEntity;

public class InvoiceMapper {

  private InvoiceMapper() {
  }

  public static Invoice toDomain(InvoiceEntity entity) {
    if (entity == null) return null;

    Payment payment = PaymentMapper.toDomain(entity.getPayment());
    List<InvoiceDetail> details = entity.getDetails()
        .stream()
        .map(InvoiceDetailMapper::toDomain)
        .toList();

    Reservation reservation = payment != null ? payment.getReservation() : null;

    return new Invoice(
        entity.getId(),
        entity.getCode(),
        reservation,
        payment,
        entity.getIssueDate(),
        entity.getTotal(),
        details
    );
  }

  public static InvoiceResponse toResponse(Invoice invoice) {
    if (invoice == null) return null;

    List<InvoiceDetailResponse> details = invoice.getDetails()
        .stream()
        .map(InvoiceDetailMapper::toResponse)
        .toList();

    Long paymentId = invoice.getPayment() != null ? invoice.getPayment().getId() : null;
    Reservation reservation = invoice.getPayment() != null ? invoice.getPayment().getReservation() : null;
    Long reservationId = reservation != null ? reservation.getId() : null;
    Long clientId = reservation != null ? reservation.getClientId() : null;

    return new InvoiceResponse(
        invoice.getId(),
        invoice.getCode(),
        paymentId,
        reservationId,
        clientId,
        invoice.getIssueDate(),
        invoice.getTotal(),
        details
    );
  }
}
