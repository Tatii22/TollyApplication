package com.rentaherramientas.tolly.application.usecase.returns;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.application.dto.reservation.ReservationResponse;

import com.rentaherramientas.tolly.application.mapper.ReservationMapper;
import com.rentaherramientas.tolly.application.mapper.ReservationStatusMapper;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.ReservationStatus;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationStatusRepository;

import jakarta.transaction.Transactional;

@Service
public class SolicitudReservationUseCase {
    private final ClientRepository clientRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationStatusRepository reservationStatusRepository;
    private final ReservationMapper reservationMapper;
    private final ReservationStatusMapper reservationStatusMapper;

    public SolicitudReservationUseCase(ClientRepository clientRepository, ReservationRepository reservationRepository,
            ReservationStatusRepository reservationStatusRepository, ReservationMapper reservationMapper,
            ReservationStatusMapper reservationStatusMapper) {
        this.clientRepository = clientRepository;
        this.reservationRepository = reservationRepository;
        this.reservationStatusRepository = reservationStatusRepository;
        this.reservationMapper = reservationMapper;
        this.reservationStatusMapper = reservationStatusMapper;
    }

    @Transactional
    public List<ReservationResponse> aprobarReserva(String statusName, long idReserva ) {

        Reservation idReservar = reservationRepository.findById(idReserva)
                .orElseThrow(() -> new DomainException("Reserva no encontrada con ID " + idReserva + " no encontrada"));

                if (idReservar == null ){
                    throw new DomainException("reserva no encontrada");
                }
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



    private ReservationResponse mapToResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getClientId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getTotal(),
                reservation.getStatus().getName(),
                reservation.getCreatedAt());
    }

}
