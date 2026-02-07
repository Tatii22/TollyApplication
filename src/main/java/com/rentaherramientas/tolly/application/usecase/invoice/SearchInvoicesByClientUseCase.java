package com.rentaherramientas.tolly.application.usecase.invoice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Client;
import com.rentaherramientas.tolly.domain.model.Invoice;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.domain.ports.InvoiceRepository;

@Service
public class SearchInvoicesByClientUseCase {

  private final InvoiceRepository invoiceRepository;
  private final ClientRepository clientRepository;

  public SearchInvoicesByClientUseCase(InvoiceRepository invoiceRepository,
                                       ClientRepository clientRepository) {
    this.invoiceRepository = invoiceRepository;
    this.clientRepository = clientRepository;
  }

  public List<Invoice> execute(Long clientId,
                               UUID userId,
                               boolean validateOwner,
                               LocalDateTime from,
                               LocalDateTime to,
                               String paymentStatus) {
    if (clientId == null) {
      throw new DomainException("ClientId is required");
    }
    if (validateOwner) {
      if (userId == null) {
        throw new DomainException("UserId is required");
      }
      Client client = clientRepository.findByUserId(User.restore(userId))
          .orElseThrow(() -> new DomainException("Client not found for user"));
      if (!clientId.equals(client.getId())) {
        throw new DomainException("Client does not match");
      }
    }
    return invoiceRepository.searchByClientId(clientId, from, to, paymentStatus);
  }
}
