package com.rentaherramientas.tolly.application.usecase.Reservation;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.ReservationStatus;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationStatusRepository;
import com.rentaherramientas.tolly.application.dto.reservation.ReservationRequest;
import com.rentaherramientas.tolly.application.dto.reservation.ReservationResponse;

@Service
public class ReservationCreateUseCase {

  private final ReservationRepository reservationRepository;
  private final ReservationStatusRepository reservationStatusRepository;
  private final ClientRepository clientRepository;

  public ReservationCreateUseCase(ReservationRepository reservationRepository,
      ReservationStatusRepository reservationStatusRepository,
      ClientRepository clientRepository) {
    this.reservationRepository = reservationRepository;
    this.reservationStatusRepository = reservationStatusRepository;
    this.clientRepository = clientRepository;
  }

  // --------------------- CREATE RESERVA ---------------------
  @Transactional
  public ReservationResponse createReservation(ReservationRequest request) {
    // 1️⃣ Verificar que el cliente exista usando ClientRepository
    clientRepository.findById(request.clientId())
        .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + request.clientId()));

    // 2️⃣ Buscar o crear el estado de la reserva
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

    // 4️⃣ Guardar la reserva usando el adaptador
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
