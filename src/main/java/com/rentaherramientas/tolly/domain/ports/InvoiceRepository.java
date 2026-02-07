package com.rentaherramientas.tolly.domain.ports;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.rentaherramientas.tolly.domain.model.Invoice;

public interface InvoiceRepository {
  Invoice save(Invoice invoice);
  Optional<Invoice> findById(Long id);
  Optional<Invoice> findByPaymentId(Long paymentId);
  boolean existsByPaymentId(Long paymentId);
  List<Invoice> findByClientId(Long clientId);
  List<Invoice> findBySupplierId(Long supplierId);
  List<Invoice> findAll();
  List<Invoice> searchAll(LocalDateTime from, LocalDateTime to, String paymentStatus);
  List<Invoice> searchByClientId(Long clientId, LocalDateTime from, LocalDateTime to, String paymentStatus);
  List<Invoice> searchBySupplierId(Long supplierId, LocalDateTime from, LocalDateTime to, String paymentStatus);
}
