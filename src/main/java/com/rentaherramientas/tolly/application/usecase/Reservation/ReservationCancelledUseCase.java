package com.rentaherramientas.tolly.application.usecase.reservation;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.ReservationStatus;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationStatusRepository;
import com.rentaherramientas.tolly.application.dto.reservation.ReservationResponse;

@Service
public class ReservationCancelledUseCase {

    private final ReservationRepository reservationRepository;
    private final ReservationStatusRepository reservationStatusRepository;

    public ReservationCancelledUseCase(ReservationRepository reservationRepository,
                              ReservationStatusRepository reservationStatusRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationStatusRepository = reservationStatusRepository;
    }

    // --------------------- CANCELAR RESERVA ---------------------
    @Transactional
    public ReservationResponse cancelReservation(Long reservationId) {
        // 1️⃣ Buscar la reserva
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con ID: " + reservationId));

        // 2️⃣ Verificar que la reserva esté en IN_PROGRESS
        if (!"IN_PROGRESS".equalsIgnoreCase(reservation.getStatus().getName())) {
            throw new IllegalStateException("Solo se pueden cancelar reservas en estado IN_PROGRESS");
        }

        // 3️⃣ Obtener el estado FINISHED de la tabla de estados
        ReservationStatus finishedStatus = reservationStatusRepository.findByStatusName("FINISHED")
                .orElseThrow(() -> new IllegalArgumentException("Estado FINISHED no encontrado en la base de datos"));

        // 4️⃣ Actualizar el estado de la reserva
        reservation.setStatus(finishedStatus);

        // 5️⃣ Guardar la reserva con el nuevo estado
        Reservation updated = reservationRepository.save(reservation);

        // 6️⃣ Devolver DTO actualizado
        return mapToResponse(updated);
    }

    // --------------------- MAPPER INTERNO ---------------------
    private ReservationResponse mapToResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getClientId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getTotal(),
                reservation.getStatus().getName(),
                reservation.getCreatedAt()
        );
    }
}
