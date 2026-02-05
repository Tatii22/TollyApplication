package com.rentaherramientas.tolly.domain.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.rentaherramientas.tolly.domain.model.Reservation;

public interface ReservationRepository {

  // Crear o actualizar
    Reservation save(Reservation reservation);

    // Leer
    Optional<Reservation> findById(Long id);

    // Borrar
    void delete(Reservation reservation);

    // Listar todas
    List<Reservation> findAll();

    // Listar por cliente
    List<Reservation> findByClientId(UUID clientId);

}
