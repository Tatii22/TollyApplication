package com.rentaherramientas.tolly.application.usecase.reservationdetail;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.model.ReservationDetail;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.ports.ReservationDetailRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ToolRepository;

@Service
@Transactional
public class DeleteReservationDetailUseCase {

  private final ReservationDetailRepository reservationDetailRepository;
  private final ReservationRepository reservationRepository;
  private final ToolRepository toolRepository;

  public DeleteReservationDetailUseCase(
      ReservationDetailRepository reservationDetailRepository,
      ReservationRepository reservationRepository,
      ToolRepository toolRepository) {
    this.reservationDetailRepository = reservationDetailRepository;
    this.reservationRepository = reservationRepository;
    this.toolRepository = toolRepository;
  }

  public void execute(Long detailId) {

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
        throw new RuntimeException("No se puede eliminar detalles de una reserva " + statusName);
      }
    }

    reservationDetailRepository.delete(detail);

    if (detail.getTool() != null && detail.getTool().getId() != null) {
      toolRepository.findById(detail.getTool().getId())
          .ifPresent(tool -> {
            Integer available = tool.getAvailableQuantity() != null ? tool.getAvailableQuantity() : 0;
            tool.setAvailableQuantity(available + detail.getQuantity());
            toolRepository.update(tool.getId(), tool);
          });
    }
  }
}
