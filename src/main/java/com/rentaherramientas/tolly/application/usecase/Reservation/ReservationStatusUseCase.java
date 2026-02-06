package com.rentaherramientas.tolly.application.usecase.Reservation;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.ReservationStatus;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationStatusRepository;
import com.rentaherramientas.tolly.application.dto.reservation.ReservationResponse;

@Service
public class ReservationStatusUseCase {

    private final ReservationRepository reservationRepository;
    private final ReservationStatusRepository reservationStatusRepository;

    public ReservationStatusUseCase(ReservationRepository reservationRepository,
                              ReservationStatusRepository reservationStatusRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationStatusRepository = reservationStatusRepository;
    }

    // --------------------- LISTAR RESERVAS POR ESTADO ---------------------
    public List<ReservationResponse> getReservationsByStatus(String statusName) {
        // 1️⃣ Buscar el estado en la tabla de estados
        ReservationStatus status = reservationStatusRepository.findByStatusName(statusName)
                .orElseThrow(() -> new IllegalArgumentException("Estado de reserva no encontrado: " + statusName));

        // 2️⃣ Obtener todas las reservas y filtrar por el estado
        List<Reservation> reservations = reservationRepository.findAll()
                .stream()
                .filter(r -> r.getStatus().equals(status))
                .collect(Collectors.toList());

        // 3️⃣ Mapear a DTO
        return reservations.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
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
