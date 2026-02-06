package com.rentaherramientas.tolly.application.usecase.reservationdetail;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.model.ReservationDetail;
import com.rentaherramientas.tolly.domain.ports.ReservationDetailRepository;

@Service
@Transactional
public class UpdateReservationDetailUseCase {

  private final ReservationDetailRepository reservationDetailRepository;

  public UpdateReservationDetailUseCase(
      ReservationDetailRepository reservationDetailRepository) {
    this.reservationDetailRepository = reservationDetailRepository;
  }

  public ReservationDetail execute(
      BigDecimal detailId,
      int newRentalDays) {

    // 1️ Buscar el detalle existente
    ReservationDetail existingDetail =
        reservationDetailRepository.findById(detailId)
            .orElseThrow(() -> new RuntimeException("Detalle de reserva no encontrado"));

    if (newRentalDays < 1) {
      throw new RuntimeException("Los días de alquiler deben ser mayores a 0");
    }

    // 2️ Recrear el detalle con los nuevos días
    ReservationDetail updatedDetail =
        ReservationDetail.reconstruct(
            existingDetail.getId(),
            existingDetail.getTool(),
            existingDetail.getReservation(),
            existingDetail.getDailyPrice(),
            newRentalDays,
            null //  el dominio recalcula el subtotal
        );

    // 3️ Guardar cambios
    return reservationDetailRepository.save(updatedDetail);
  }
}
