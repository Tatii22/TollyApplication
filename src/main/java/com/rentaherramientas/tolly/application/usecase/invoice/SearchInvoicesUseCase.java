package com.rentaherramientas.tolly.application.usecase.invoice;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.domain.model.Invoice;
import com.rentaherramientas.tolly.domain.ports.InvoiceRepository;

@Service
public class SearchInvoicesUseCase {

  private final InvoiceRepository invoiceRepository;

  public SearchInvoicesUseCase(InvoiceRepository invoiceRepository) {
    this.invoiceRepository = invoiceRepository;
  }

  public List<Invoice> execute(LocalDateTime from, LocalDateTime to, String paymentStatus) {
    return invoiceRepository.searchAll(from, to, paymentStatus);
  }
}
