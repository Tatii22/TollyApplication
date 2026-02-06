package com.rentaherramientas.tolly.application.usecase.reservationdetail;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.model.ReservationDetail;
import com.rentaherramientas.tolly.domain.ports.ReservationDetailRepository;

@Service
@Transactional
public class UpdateReservationDetailRentalDaysUseCase {

  private final ReservationDetailRepository reservationDetailRepository;

  public UpdateReservationDetailRentalDaysUseCase(
      ReservationDetailRepository reservationDetailRepository) {
    this.reservationDetailRepository = reservationDetailRepository;
  }

  public ReservationDetail execute(BigDecimal detailId, int newRentalDays) {

    ReservationDetail detail = reservationDetailRepository.findById(detailId)
        .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

    BigDecimal newSubTotal =
        BigDecimal.valueOf(detail.getDailyPrice())
            .multiply(BigDecimal.valueOf(newRentalDays));

    ReservationDetail updated = ReservationDetail.reconstruct(
        detail.getId(),
        detail.getTool(),
        detail.getReservation(),
        detail.getDailyPrice(),
        newRentalDays,
        newSubTotal
    );

    return reservationDetailRepository.save(updated);
  }
}
