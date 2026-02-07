package com.rentaherramientas.tolly.application.usecase.invoice;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Client;
import com.rentaherramientas.tolly.domain.model.Invoice;
import com.rentaherramientas.tolly.domain.model.Payment;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.domain.ports.InvoiceRepository;
import com.rentaherramientas.tolly.domain.ports.PaymentRepository;

@Service
public class GetInvoiceByPaymentUseCase {

  private final InvoiceRepository invoiceRepository;
  private final PaymentRepository paymentRepository;
  private final ClientRepository clientRepository;

  public GetInvoiceByPaymentUseCase(InvoiceRepository invoiceRepository,
                                    PaymentRepository paymentRepository,
                                    ClientRepository clientRepository) {
    this.invoiceRepository = invoiceRepository;
    this.paymentRepository = paymentRepository;
    this.clientRepository = clientRepository;
  }

  public Invoice execute(Long paymentId, UUID userId, boolean validateOwner) {
    if (paymentId == null) {
      throw new DomainException("PaymentId is required");
    }

    if (validateOwner) {
      if (userId == null) {
        throw new DomainException("UserId is required");
      }
      Payment payment = paymentRepository.findById(paymentId)
          .orElseThrow(() -> new DomainException("Payment not found: " + paymentId));
      Reservation reservation = payment.getReservation();
      Client client = clientRepository.findByUserId(User.restore(userId))
          .orElseThrow(() -> new DomainException("Client not found for user"));
      if (reservation == null || !client.getId().equals(reservation.getClientId())) {
        throw new DomainException("Invoice does not belong to client");
      }
    }

    return invoiceRepository.findByPaymentId(paymentId)
        .orElseThrow(() -> new DomainException("Invoice not found for payment: " + paymentId));
  }
}
