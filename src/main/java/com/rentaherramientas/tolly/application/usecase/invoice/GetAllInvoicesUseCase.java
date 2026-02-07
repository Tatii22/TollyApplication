package com.rentaherramientas.tolly.application.usecase.invoice;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.domain.model.Invoice;
import com.rentaherramientas.tolly.domain.ports.InvoiceRepository;

@Service
public class GetAllInvoicesUseCase {

  private final InvoiceRepository invoiceRepository;

  public GetAllInvoicesUseCase(InvoiceRepository invoiceRepository) {
    this.invoiceRepository = invoiceRepository;
  }

  public List<Invoice> execute() {
    return invoiceRepository.findAll();
  }
}
