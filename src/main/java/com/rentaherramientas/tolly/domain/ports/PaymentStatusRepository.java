package com.rentaherramientas.tolly.domain.ports;

import java.util.List;
import java.util.Optional;

import com.rentaherramientas.tolly.domain.model.PaymentStatus;

public interface PaymentStatusRepository {

    // CREATE / UPDATE
    PaymentStatus save(PaymentStatus status);

    // READ
    Optional<PaymentStatus> findById(Long id);

    Optional<PaymentStatus> findByName(String name);

    List<PaymentStatus> findAll();

    // DELETE
    void delete(PaymentStatus status);

    void deleteById(Long id);
}
