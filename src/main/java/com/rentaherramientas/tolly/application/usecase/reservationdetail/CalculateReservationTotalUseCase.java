package com.rentaherramientas.tolly.application.usecase.reservationdetail;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.model.ReservationDetail;
import com.rentaherramientas.tolly.domain.ports.ReservationDetailRepository;

@Service
@Transactional(readOnly = true)
public class CalculateReservationTotalUseCase {

  private final ReservationDetailRepository reservationDetailRepository;

  public CalculateReservationTotalUseCase(
      ReservationDetailRepository reservationDetailRepository) {
    this.reservationDetailRepository = reservationDetailRepository;
  }

  public BigDecimal execute(Long reservationId) {

    List<ReservationDetail> details =
        reservationDetailRepository.findByReservationId(reservationId);

    return details.stream()
        .map(ReservationDetail::getSubTotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }
}
