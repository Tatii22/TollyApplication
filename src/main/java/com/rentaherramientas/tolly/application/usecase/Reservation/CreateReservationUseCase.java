package com.rentaherramientas.tolly.application.usecase.reservation;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.application.dto.reservation.ReservationRequest;
import com.rentaherramientas.tolly.application.dto.reservation.ReservationResponse;
import com.rentaherramientas.tolly.domain.model.Client;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.ReservationStatus;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationStatusRepository;

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
    // 1ï¸âƒ£ Buscar el cliente
    Client client = clientRepository.findById(request.clientId())
        .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + request.clientId()));

    // 2ï¸âƒ£ Buscar o crear el estado de reserva
    ReservationStatus status = reservationStatusRepository.findByStatusName(request.statusName())
        .orElseGet(() -> ReservationStatus.create(request.statusName()));

    // Guardar el estado si es nuevo
    status = reservationStatusRepository.save(status);

    // 3ï¸âƒ£ Crear la reserva en el dominio
    Reservation reservation = Reservation.create(
        client.getId(),
        request.startDate(),
        request.endDate(),
        request.totalPrice(),
        status,
        LocalDate.now() // fecha de creaciÃ³n
    );

    // 4ï¸âƒ£ Guardar la reserva usando el adapter
    Reservation saved = reservationRepository.save(reservation);

    // 5ï¸âƒ£ Devolver DTO de respuesta
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
