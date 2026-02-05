package com.rentaherramientas.tolly.application.usecase.Reservation;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.application.dto.reservation.ReservationRequest;
import com.rentaherramientas.tolly.application.dto.reservation.ReservationResponse;
import com.rentaherramientas.tolly.domain.model.Client;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.ReservationStatus;
import com.rentaherramientas.tolly.domain.model.Role;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationStatusRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ClientEntity;

@Service
public class CreateReservationUseCase {

  private final ReservationRepository reservationRepository;
  private final ClientRepository clientRepository;
  private final ReservationStatusRepository reservationStatusRepository;

  public CreateReservationUseCase(
      ReservationRepository reservationRepository,
      ClientRepository clientRepository,
      ReservationStatusRepository reservationStatusRepository) {
    this.reservationRepository = reservationRepository;
    this.clientRepository = clientRepository;
    this.reservationStatusRepository = reservationStatusRepository;
  }

  @Transactional
  public ReservationResponse createReservation(ReservationRequest request) {
    // 1️⃣ Buscar el cliente
    User user = new User(request.clientId());
    Client client = clientRepository.findByUserId(user)
        .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + request.clientId()));

    // 2️⃣ Buscar o crear el estado de reserva
    ReservationStatus status = reservationStatusRepository.findByStatusName(request.statusName())
        .orElseGet(() -> ReservationStatus.create(request.statusName()));

    // Guardar el estado si es nuevo
    status = reservationStatusRepository.save(status);

    // 3️⃣ Crear la reserva en el dominio
    Reservation reservation = Reservation.create(
        request.clientId(),
        request.startDate(),
        request.endDate(),
        request.totalPrice(),
        status,
        LocalDate.now() // fecha de creación
    );

    // 4️⃣ Guardar la reserva usando el adapter
    Reservation saved = reservationRepository.save(reservation);

    // 5️⃣ Devolver DTO de respuesta
    return new ReservationResponse(
        saved.getId(),
        saved.getClientId(),
        saved.getStartDate(),
        saved.getEndDate(),
        saved.getTotal(),
        saved.getStatus().getName(),
        saved.getCreatedAt());
  }
}
