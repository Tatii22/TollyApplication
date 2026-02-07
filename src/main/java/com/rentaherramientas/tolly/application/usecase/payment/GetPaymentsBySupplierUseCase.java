package com.rentaherramientas.tolly.application.usecase.payment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Supplier;
import com.rentaherramientas.tolly.domain.model.Payment;
import com.rentaherramientas.tolly.domain.ports.PaymentRepository;
import com.rentaherramientas.tolly.domain.ports.SupplierRepository;

@Service
public class GetPaymentsBySupplierUseCase {

  private final PaymentRepository paymentRepository;
  private final SupplierRepository supplierRepository;

  public GetPaymentsBySupplierUseCase(PaymentRepository paymentRepository,
                                      SupplierRepository supplierRepository) {
    this.paymentRepository = paymentRepository;
    this.supplierRepository = supplierRepository;
  }

  public List<Payment> execute(Long supplierId, UUID userId, boolean validateOwner,
                               LocalDateTime from, LocalDateTime to) {
    if (supplierId == null) {
      throw new DomainException("SupplierId is required");
    }

    if (validateOwner) {
      if (userId == null) {
        throw new DomainException("UserId is required");
      }
      Supplier supplier = supplierRepository.findByUserId(userId)
          .orElseThrow(() -> new DomainException("Proveedor no encontrado para el usuario"));
      if (!supplier.getId().equals(supplierId)) {
        throw new DomainException("No tienes permiso para ver pagos de otro proveedor");
      }
    }

    return paymentRepository.findBySupplierIdAndDateRange(supplierId, from, to);
  }
}
