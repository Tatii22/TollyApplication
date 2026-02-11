package com.rentaherramientas.tolly.application.usecase.reservation;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.application.dto.payment.CreatePaymentRequest;
import com.rentaherramientas.tolly.application.dto.reservation.ReservationRequest;
import com.rentaherramientas.tolly.application.dto.reservation.ReservationResponse;
import com.rentaherramientas.tolly.application.usecase.payment.CreatePaymentUseCase;
import com.rentaherramientas.tolly.domain.model.Client;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.ReservationStatus;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationStatusRepository;
import java.util.UUID;

@Service
public class CreateReservationUseCase {

  private final ReservationRepository reservationRepository;
  private final ClientRepository clientRepository;
  private final ReservationStatusRepository reservationStatusRepository;
  private final CreatePaymentUseCase createPaymentUseCase;

  public CreateReservationUseCase(
      ReservationRepository reservationRepository,
      ClientRepository clientRepository,
      ReservationStatusRepository reservationStatusRepository,
      CreatePaymentUseCase createPaymentUseCase) {
    this.reservationRepository = reservationRepository;
    this.clientRepository = clientRepository;
    this.reservationStatusRepository = reservationStatusRepository;
    this.createPaymentUseCase = createPaymentUseCase;
  }

  @Transactional
  public ReservationResponse createReservation(ReservationRequest request, UUID userId) {
    if (request.startDate() == null || request.endDate() == null) {
      throw new IllegalArgumentException("startDate y endDate son obligatorias");
    }
    if (request.startDate().isAfter(request.endDate())) {
      throw new IllegalArgumentException("startDate no puede ser mayor que endDate");
    }
    // 1ï¸âƒ£ Buscar el cliente
    if (userId == null) {
      throw new IllegalArgumentException("UserId is required");
    }
    Client client = clientRepository.findByUserId(User.restore(userId))
        .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado para el usuario"));

    // 2ï¸âƒ£ Buscar o crear el estado de reserva
    ReservationStatus status = reservationStatusRepository.findByStatusName("PENDIENTE")
        .orElseGet(() -> ReservationStatus.create("PENDIENTE"));

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

    // 5) Crear pago automaticamente en estado PENDING
    createPaymentUseCase.execute(new CreatePaymentRequest(saved.getId(), saved.getTotal()));

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
