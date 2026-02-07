package com.rentaherramientas.tolly.application.usecase.report;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.application.dto.reservation.ReservationResponse;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;

@Service
public class GetRentalHistoryReportUseCase {

  private final ReservationRepository reservationRepository;

  public GetRentalHistoryReportUseCase(ReservationRepository reservationRepository) {
    this.reservationRepository = reservationRepository;
  }

  public List<ReservationResponse> execute(LocalDate from, LocalDate to) {
    return reservationRepository.findByStartDateRange(from, to).stream()
        .map(this::toResponse)
        .toList();
  }

  private ReservationResponse toResponse(Reservation reservation) {
    return new ReservationResponse(
        reservation.getId(),
        reservation.getClientId(),
        reservation.getStartDate(),
        reservation.getEndDate(),
        reservation.getTotal(),
        reservation.getStatus() != null ? reservation.getStatus().getName() : null,
        reservation.getCreatedAt());
  }
}
