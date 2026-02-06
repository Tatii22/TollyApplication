package com.rentaherramientas.tolly.application.usecase.reservationdetail;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.model.ReservationDetail;
import com.rentaherramientas.tolly.domain.ports.ReservationDetailRepository;

@Service
@Transactional(readOnly = true)
public class GetReservationDetailsByReservationUseCase {

  private final ReservationDetailRepository reservationDetailRepository;

  public GetReservationDetailsByReservationUseCase(
      ReservationDetailRepository reservationDetailRepository) {
    this.reservationDetailRepository = reservationDetailRepository;
  }

  public List<ReservationDetail> execute(Long reservationId) {
    return reservationDetailRepository.findByReservationId(reservationId);
  }
}
