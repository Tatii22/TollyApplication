package com.rentaherramientas.tolly.domain.ports;

import java.util.List;
import java.util.Optional;

import com.rentaherramientas.tolly.domain.model.Payment;

public interface PaymentRepository {

    // CREATE / UPDATE
    Payment save(Payment payment);

    // READ
    Optional<Payment> findById(Long id);

    List<Payment> findAll();

    // DELETE
    void delete(Payment payment);

    void deleteById(Long id);

    // QUERIES DE NEGOCIO
    List<Payment> findByReservationId(Long reservationId);

    List<Payment> findByStatusName(String statusName);
}
