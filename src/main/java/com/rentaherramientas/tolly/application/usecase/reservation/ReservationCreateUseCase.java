package com.rentaherramientas.tolly.application.usecase.reservation;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.application.dto.payment.CreatePaymentRequest;
import com.rentaherramientas.tolly.application.dto.reservation.ReservationRequest;
import com.rentaherramientas.tolly.application.dto.reservation.ReservationResponse;
import com.rentaherramientas.tolly.application.usecase.payment.CreatePaymentUseCase;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.ReservationStatus;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationStatusRepository;

@Service
public class ReservationCreateUseCase {

  private final ReservationRepository reservationRepository;
  private final ReservationStatusRepository reservationStatusRepository;
  private final ClientRepository clientRepository;
  private final CreatePaymentUseCase createPaymentUseCase;

  public ReservationCreateUseCase(ReservationRepository reservationRepository,
      ReservationStatusRepository reservationStatusRepository,
      ClientRepository clientRepository,
      CreatePaymentUseCase createPaymentUseCase) {
    this.reservationRepository = reservationRepository;
    this.reservationStatusRepository = reservationStatusRepository;
    this.clientRepository = clientRepository;
    this.createPaymentUseCase = createPaymentUseCase;
  }

  @Transactional
  public ReservationResponse createReservation(ReservationRequest request) {
    clientRepository.findById(request.clientId())
        .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + request.clientId()));

    ReservationStatus status = reservationStatusRepository.findByStatusName("PENDIENTE")
        .orElseGet(() -> ReservationStatus.create("PENDIENTE"));

    status = reservationStatusRepository.save(status);

    Reservation reservation = Reservation.create(
        request.clientId(),
        request.startDate(),
        request.endDate(),
        request.totalPrice(),
        status,
        LocalDate.now()
    );

    Reservation saved = reservationRepository.save(reservation);

    createPaymentUseCase.execute(new CreatePaymentRequest(saved.getId(), saved.getTotal()));

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
