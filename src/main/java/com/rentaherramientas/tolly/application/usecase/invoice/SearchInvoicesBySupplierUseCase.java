package com.rentaherramientas.tolly.application.usecase.invoice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Invoice;
import com.rentaherramientas.tolly.domain.model.Supplier;
import com.rentaherramientas.tolly.domain.ports.InvoiceRepository;
import com.rentaherramientas.tolly.domain.ports.SupplierRepository;

@Service
public class SearchInvoicesBySupplierUseCase {

  private final InvoiceRepository invoiceRepository;
  private final SupplierRepository supplierRepository;

  public SearchInvoicesBySupplierUseCase(InvoiceRepository invoiceRepository,
                                         SupplierRepository supplierRepository) {
    this.invoiceRepository = invoiceRepository;
    this.supplierRepository = supplierRepository;
  }

  public List<Invoice> execute(Long supplierId,
                               UUID userId,
                               boolean validateOwner,
                               LocalDateTime from,
                               LocalDateTime to,
                               String paymentStatus) {
    if (supplierId == null) {
      throw new DomainException("SupplierId is required");
    }
    if (validateOwner) {
      if (userId == null) {
        throw new DomainException("UserId is required");
      }
      Supplier supplier = supplierRepository.findByUserId(userId)
          .orElseThrow(() -> new DomainException("Supplier not found for user"));
      if (!supplierId.equals(supplier.getId())) {
        throw new DomainException("Supplier does not match");
      }
    }
    return invoiceRepository.searchBySupplierId(supplierId, from, to, paymentStatus);
  }
}
