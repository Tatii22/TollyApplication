package com.rentaherramientas.tolly.domain.ports;

import java.util.List;
import java.util.Optional;

import com.rentaherramientas.tolly.domain.model.Return;

public interface ReturnRepository {

    // CREATE / UPDATE
    Return save(Return ret);

    // READ
    Optional<Return> findById(Long id);

    List<Return> findAll();

    // DELETE
    void delete(Return ret);

    void deleteById(Long id);

    // EXTRA (útiles según el modelo)
    List<Return> findByClientId(Long clientId);

    List<Return> findByReservationId(Long reservationId);
}
