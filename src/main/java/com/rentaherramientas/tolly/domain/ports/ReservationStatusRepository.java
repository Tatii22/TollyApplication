package com.rentaherramientas.tolly.domain.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.rentaherramientas.tolly.domain.model.ReservationStatus;

public interface ReservationStatusRepository {

    // Crear o actualizar
    ReservationStatus save(ReservationStatus reservationStatus);

    // Buscar por nombre
    Optional<ReservationStatus> findByName(String name);

    // Buscar por ID
    Optional<ReservationStatus> findById(UUID id);

    // Listar todos los estados
    List<ReservationStatus> findAll();

    // Borrar
    void delete(ReservationStatus reservationStatus);
}
