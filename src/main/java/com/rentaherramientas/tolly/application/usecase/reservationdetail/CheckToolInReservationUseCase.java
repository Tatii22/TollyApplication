package com.rentaherramientas.tolly.application.usecase.reservationdetail;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.ports.ReservationDetailRepository;

@Service
@Transactional(readOnly = true)
public class CheckToolInReservationUseCase {

  private final ReservationDetailRepository reservationDetailRepository;

  public CheckToolInReservationUseCase(
      ReservationDetailRepository reservationDetailRepository) {
    this.reservationDetailRepository = reservationDetailRepository;
  }

  public boolean execute(Long reservationId, Long toolId) {
    return reservationDetailRepository
        .existsByReservationIdAndToolId(reservationId, toolId);
  }
}
