package com.rentaherramientas.tolly.application.usecase.invoice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Invoice;
import com.rentaherramientas.tolly.domain.model.InvoiceDetail;
import com.rentaherramientas.tolly.domain.model.Payment;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.ReservationDetail;
import com.rentaherramientas.tolly.domain.ports.InvoiceRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationDetailRepository;

@Service
public class GenerateInvoiceForPaymentUseCase {

  private final InvoiceRepository invoiceRepository;
  private final ReservationDetailRepository reservationDetailRepository;

  public GenerateInvoiceForPaymentUseCase(InvoiceRepository invoiceRepository,
                                          ReservationDetailRepository reservationDetailRepository) {
    this.invoiceRepository = invoiceRepository;
    this.reservationDetailRepository = reservationDetailRepository;
  }

  @Transactional
  public Invoice execute(Payment payment) {
    if (payment == null || payment.getId() == null) {
      throw new DomainException("Payment is required");
    }
    if (!payment.isPaid()) {
      throw new DomainException("Invoice can only be generated for PAID payments");
    }

    if (invoiceRepository.existsByPaymentId(payment.getId())) {
      return invoiceRepository.findByPaymentId(payment.getId())
          .orElseThrow(() -> new DomainException("Invoice already exists but not found"));
    }

    Reservation reservation = payment.getReservation();
    if (reservation == null || reservation.getId() == null) {
      throw new DomainException("Reservation is required for invoice");
    }

    List<ReservationDetail> reservationDetails =
        reservationDetailRepository.findByReservationId(reservation.getId());
    if (reservationDetails.isEmpty()) {
      throw new DomainException("Reservation does not have details to invoice");
    }

    List<InvoiceDetail> details = reservationDetails.stream()
        .map(detail -> new InvoiceDetail(
            null,
            null,
            detail.getTool(),
            BigDecimal.valueOf(detail.getDailyPrice()),
            detail.getRentalDay(),
            detail.getSubTotal()
        ))
        .toList();

    BigDecimal total = payment.getAmount() != null
        ? payment.getAmount()
        : reservationDetails.stream()
            .map(ReservationDetail::getSubTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    LocalDateTime issueDate = payment.getPaymentDate() != null
        ? payment.getPaymentDate()
        : LocalDateTime.now();

    Invoice invoice = new Invoice(
        null,
        reservation,
        payment,
        issueDate,
        total,
        details
    );

    return invoiceRepository.save(invoice);
  }
}
