package com.rentaherramientas.tolly.domain.ports;

import com.rentaherramientas.tolly.domain.model.Payment;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findById(Long id);

    Optional<Payment> findByReservationId(Long reservationId);

    boolean existsByReservationId(Long reservationId);

    List<Payment> findByClientId(Long clientId);

    List<Payment> findByStatusName(String statusName);

    List<Payment> findAll();

    List<Payment> findBySupplierIdAndDateRange(Long supplierId, LocalDateTime from, LocalDateTime to);

    List<Payment> findByDateRange(LocalDateTime from, LocalDateTime to, String statusName);
}
