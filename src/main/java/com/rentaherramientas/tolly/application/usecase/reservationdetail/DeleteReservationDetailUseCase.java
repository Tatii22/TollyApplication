package com.rentaherramientas.tolly.application.usecase.reservationdetail;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.model.ReservationDetail;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.Payment;
import com.rentaherramientas.tolly.domain.model.Tool;
import com.rentaherramientas.tolly.domain.ports.PaymentRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationDetailRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ToolRepository;

@Service
@Transactional
public class DeleteReservationDetailUseCase {

  private final ReservationDetailRepository reservationDetailRepository;
  private final ReservationRepository reservationRepository;
  private final PaymentRepository paymentRepository;
  private final ToolRepository toolRepository;
  public DeleteReservationDetailUseCase(
      ReservationDetailRepository reservationDetailRepository,
      ReservationRepository reservationRepository,
      PaymentRepository paymentRepository,
      ToolRepository toolRepository) {
    this.reservationDetailRepository = reservationDetailRepository;
    this.reservationRepository = reservationRepository;
    this.paymentRepository = paymentRepository;
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

    paymentRepository.findByReservationId(reservation.getId())
        .ifPresent(payment -> {
          if (payment.isPaid()) {
            throw new RuntimeException("No se puede modificar una reserva ya pagada");
          }
        });

    if (reservation.getStatus() != null) {
      String statusName = reservation.getStatus().getName();
      if ("CANCELLED".equalsIgnoreCase(statusName)
          || "IN_INCIDENT".equalsIgnoreCase(statusName)
          || "FINISHED".equalsIgnoreCase(statusName)) {
        throw new RuntimeException("No se puede eliminar detalles de una reserva " + statusName);
      }
    }

    reservationDetailRepository.delete(detail);

    adjustToolAvailability(detail.getTool(), detail.getQuantity());
    updateReservationAndPaymentTotal(reservation);

    // No se ajusta availableQuantity: la disponibilidad se calcula por fechas
  }

  private void updateReservationAndPaymentTotal(Reservation reservation) {
    BigDecimal total = reservationDetailRepository.findByReservationId(reservation.getId())
        .stream()
        .map(ReservationDetail::getSubTotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    reservation.setTotal(total);
    reservationRepository.save(reservation);

    paymentRepository.findByReservationId(reservation.getId())
        .ifPresent(payment -> {
          if (payment.isPending() && payment.getAmount().compareTo(total) != 0) {
            Payment updated = new Payment(
                payment.getId(),
                reservation,
                total,
                payment.getPaymentDate(),
                payment.getStatus()
            );
            paymentRepository.save(updated);
          }
        });
  }

  private void adjustToolAvailability(Tool tool, int delta) {
    if (tool == null || tool.getId() == null) {
      return;
    }

    Integer available = tool.getAvailableQuantity();
    if (available == null) {
      Integer total = tool.getTotalQuantity();
      if (total == null) {
        return;
      }
      available = total;
    }

    int updated = available + delta;
    if (updated < 0) {
      throw new RuntimeException("La cantidad disponible no puede ser negativa");
    }

    tool.setAvailableQuantity(updated);
    toolRepository.update(tool.getId(), tool);
  }
}
