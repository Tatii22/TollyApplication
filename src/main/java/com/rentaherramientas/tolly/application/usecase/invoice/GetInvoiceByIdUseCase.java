package com.rentaherramientas.tolly.application.usecase.invoice;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Client;
import com.rentaherramientas.tolly.domain.model.Invoice;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.Supplier;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.domain.ports.InvoiceRepository;
import com.rentaherramientas.tolly.domain.ports.SupplierRepository;

@Service
public class GetInvoiceByIdUseCase {

  private final InvoiceRepository invoiceRepository;
  private final ClientRepository clientRepository;
  private final SupplierRepository supplierRepository;

  public GetInvoiceByIdUseCase(InvoiceRepository invoiceRepository,
                               ClientRepository clientRepository,
                               SupplierRepository supplierRepository) {
    this.invoiceRepository = invoiceRepository;
    this.clientRepository = clientRepository;
    this.supplierRepository = supplierRepository;
  }

  public Invoice execute(Long invoiceId, UUID userId, boolean validateClient, boolean validateSupplier) {
    if (invoiceId == null) {
      throw new DomainException("InvoiceId is required");
    }

    Invoice invoice = invoiceRepository.findById(invoiceId)
        .orElseThrow(() -> new DomainException("Invoice not found: " + invoiceId));

    if (validateClient) {
      if (userId == null) {
        throw new DomainException("UserId is required");
      }
      Client client = clientRepository.findByUserId(User.restore(userId))
          .orElseThrow(() -> new DomainException("Client not found for user"));
      Reservation reservation = invoice.getReservation();
      if (reservation == null || !client.getId().equals(reservation.getClientId())) {
        throw new DomainException("Invoice does not belong to client");
      }
    }

    if (validateSupplier) {
      if (userId == null) {
        throw new DomainException("UserId is required");
      }
      Supplier supplier = supplierRepository.findByUserId(userId)
          .orElseThrow(() -> new DomainException("Supplier not found for user"));
      boolean anyMatch = invoice.getDetails().stream()
          .anyMatch(detail -> detail.getTool() != null
              && detail.getTool().getSupplierId() != null
              && detail.getTool().getSupplierId().equals(supplier.getId()));
      if (!anyMatch) {
        throw new DomainException("Invoice does not belong to supplier");
      }
    }

    return invoice;
  }
}
