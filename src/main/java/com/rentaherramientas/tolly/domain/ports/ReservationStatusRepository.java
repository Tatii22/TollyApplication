package com.rentaherramientas.tolly.domain.ports;

import java.util.List;
import java.util.Optional;

import com.rentaherramientas.tolly.domain.model.ReservationStatus;

public interface ReservationStatusRepository {

    // Crear o actualizar
    ReservationStatus save(ReservationStatus reservationStatus);

    // Buscar por nombre
    Optional<ReservationStatus> findByStatusName(String statusName);

    // Buscar por ID
    Optional<ReservationStatus> findById(Long id);

    // Listar todos los estados
    List<ReservationStatus> findAll();

    // Borrar
    void delete(ReservationStatus reservationStatus);
}
