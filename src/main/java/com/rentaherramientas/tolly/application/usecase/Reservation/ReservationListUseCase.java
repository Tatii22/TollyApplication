package com.rentaherramientas.tolly.application.usecase.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.application.dto.reservation.ReservationResponse;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;

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
        // Verificar que el cliente exista
        clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + clientId));

        // Buscar todas las reservas del cliente
        List<Reservation> reservations = reservationRepository.findByClientId(clientId);

        // Mapear a DTO
        return reservations.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // --------------------- LISTAR RESERVAS CON FILTROS Y PAGINACION ---------------------
    public Page<ReservationResponse> getReservationsByClient(
            Long clientId,
            String statusName,
            LocalDate from,
            LocalDate to,
            Pageable pageable) {

        clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + clientId));

        return reservationRepository.findByClientIdAndFilters(clientId, statusName, from, to, pageable)
                .map(this::mapToResponse);
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
