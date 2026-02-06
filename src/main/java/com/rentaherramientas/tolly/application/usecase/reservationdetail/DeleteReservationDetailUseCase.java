package com.rentaherramientas.tolly.application.usecase.reservationdetail;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.model.ReservationDetail;
import com.rentaherramientas.tolly.domain.ports.ReservationDetailRepository;

@Service
@Transactional
public class DeleteReservationDetailUseCase {

  private final ReservationDetailRepository reservationDetailRepository;

  public DeleteReservationDetailUseCase(
      ReservationDetailRepository reservationDetailRepository) {
    this.reservationDetailRepository = reservationDetailRepository;
  }

  public void execute(BigDecimal detailId) {

    ReservationDetail detail = reservationDetailRepository.findById(detailId)
        .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

    reservationDetailRepository.delete(detail);
  }
}
