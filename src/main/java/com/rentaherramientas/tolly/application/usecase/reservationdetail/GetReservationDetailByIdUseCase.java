package com.rentaherramientas.tolly.application.usecase.reservationdetail;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.model.ReservationDetail;
import com.rentaherramientas.tolly.domain.ports.ReservationDetailRepository;

@Service
@Transactional(readOnly = true)
public class GetReservationDetailByIdUseCase {

  private final ReservationDetailRepository reservationDetailRepository;

  public GetReservationDetailByIdUseCase(
      ReservationDetailRepository reservationDetailRepository) {
    this.reservationDetailRepository = reservationDetailRepository;
  }

  public ReservationDetail execute(Long detailId) {
    return reservationDetailRepository.findById(detailId)
        .orElseThrow(() -> new RuntimeException("Detalle de reserva no encontrado"));
  }
}
