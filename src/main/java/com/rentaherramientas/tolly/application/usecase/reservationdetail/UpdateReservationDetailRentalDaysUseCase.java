package com.rentaherramientas.tolly.application.usecase.reservationdetail;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.ReservationDetail;
import com.rentaherramientas.tolly.domain.ports.ReservationDetailRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;

@Service
@Transactional
public class UpdateReservationDetailRentalDaysUseCase {

  private final ReservationDetailRepository reservationDetailRepository;
  private final ReservationRepository reservationRepository;

  public UpdateReservationDetailRentalDaysUseCase(
      ReservationDetailRepository reservationDetailRepository,
      ReservationRepository reservationRepository) {
    this.reservationDetailRepository = reservationDetailRepository;
    this.reservationRepository = reservationRepository;
  }

  public ReservationDetail execute(Long detailId, int newRentalDays) {

    ReservationDetail detail = reservationDetailRepository.findById(detailId)
        .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

    if (detail.getReservation() == null || detail.getReservation().getId() == null) {
      throw new RuntimeException("El detalle no tiene reserva asociada");
    }
    Reservation reservation = reservationRepository.findById(detail.getReservation().getId())
        .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    if (reservation.getStatus() != null) {
      String statusName = reservation.getStatus().getName();
      if ("CANCELLED".equalsIgnoreCase(statusName)
          || "IN_INCIDENT".equalsIgnoreCase(statusName)
          || "FINISHED".equalsIgnoreCase(statusName)) {
        throw new RuntimeException("No se puede editar detalles de una reserva " + statusName);
      }
    }

    BigDecimal newSubTotal =
        BigDecimal.valueOf(detail.getDailyPrice())
            .multiply(BigDecimal.valueOf(newRentalDays))
            .multiply(BigDecimal.valueOf(detail.getQuantity()));

    ReservationDetail updated = ReservationDetail.reconstruct(
        detail.getId(),
        detail.getTool(),
        detail.getReservation(),
        detail.getDailyPrice(),
        newRentalDays,
        detail.getQuantity(),
        newSubTotal
    );

    return reservationDetailRepository.save(updated);
  }
}
