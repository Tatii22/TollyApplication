package com.rentaherramientas.tolly.domain.ports;

import java.util.List;
import java.util.Optional;

import com.rentaherramientas.tolly.domain.model.Return;

public interface ReturnRepository {

    Return save(Return value);

    Optional<Return> findById(Long id);

    List<Return> findAll();

    List<Return> findByReservationId(Long reservationId);

    void deleteById(Long id);
}
