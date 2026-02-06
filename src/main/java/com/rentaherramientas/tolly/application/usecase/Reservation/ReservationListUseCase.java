package com.rentaherramientas.tolly.application.usecase.Reservation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.application.dto.reservation.ReservationResponse;

@Service
public class ReservationListUseCase {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;

    public ReservationListUseCase(ReservationRepository reservationRepository,
                              ClientRepository clientRepository) {
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
    }

    // --------------------- LISTAR RESERVAS DE UN CLIENTE ---------------------
    public List<ReservationResponse> getReservationsByClient(Long clientId) {
        // 1️⃣ Verificar que el cliente exista
        clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + clientId));

        // 2️⃣ Buscar todas las reservas del cliente
        List<Reservation> reservations = reservationRepository.findByClientId(clientId);

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
