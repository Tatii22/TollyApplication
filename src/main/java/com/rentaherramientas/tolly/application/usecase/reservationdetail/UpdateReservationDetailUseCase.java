package com.rentaherramientas.tolly.application.usecase.reservationdetail;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.ReservationDetail;
import com.rentaherramientas.tolly.domain.ports.ReservationDetailRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;

@Service
@Transactional
public class UpdateReservationDetailUseCase {

  private final ReservationDetailRepository reservationDetailRepository;
  private final ReservationRepository reservationRepository;

  public UpdateReservationDetailUseCase(
      ReservationDetailRepository reservationDetailRepository,
      ReservationRepository reservationRepository) {
    this.reservationDetailRepository = reservationDetailRepository;
    this.reservationRepository = reservationRepository;
  }

  public ReservationDetail execute(
      Long detailId,
      int newRentalDays) {

    ReservationDetail existingDetail =
        reservationDetailRepository.findById(detailId)
            .orElseThrow(() -> new RuntimeException("Detalle de reserva no encontrado"));

    if (existingDetail.getReservation() == null || existingDetail.getReservation().getId() == null) {
      throw new RuntimeException("El detalle no tiene reserva asociada");
    }
    Reservation reservation = reservationRepository.findById(existingDetail.getReservation().getId())
        .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    if (reservation.getStatus() != null) {
      String statusName = reservation.getStatus().getName();
      if ("CANCELLED".equalsIgnoreCase(statusName)
          || "IN_INCIDENT".equalsIgnoreCase(statusName)
          || "FINISHED".equalsIgnoreCase(statusName)) {
        throw new RuntimeException("No se puede editar detalles de una reserva " + statusName);
      }
    }

    if (newRentalDays < 1) {
      throw new RuntimeException("Los dias de alquiler deben ser mayores a 0");
    }

    ReservationDetail updatedDetail =
        ReservationDetail.reconstruct(
            existingDetail.getId(),
            existingDetail.getTool(),
            existingDetail.getReservation(),
            existingDetail.getDailyPrice(),
            newRentalDays,
            existingDetail.getQuantity(),
            null
        );

    return reservationDetailRepository.save(updatedDetail);
  }
}
