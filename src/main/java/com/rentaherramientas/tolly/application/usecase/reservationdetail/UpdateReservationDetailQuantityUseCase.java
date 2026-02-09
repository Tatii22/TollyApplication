package com.rentaherramientas.tolly.application.usecase.reservationdetail;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.model.Payment;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.ReservationDetail;
import com.rentaherramientas.tolly.domain.model.Tool;
import com.rentaherramientas.tolly.domain.ports.PaymentRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationDetailRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ToolRepository;

@Service
@Transactional
public class UpdateReservationDetailQuantityUseCase {

  private final ReservationDetailRepository reservationDetailRepository;
  private final ReservationRepository reservationRepository;
  private final ToolRepository toolRepository;
  private final PaymentRepository paymentRepository;

  public UpdateReservationDetailQuantityUseCase(
      ReservationDetailRepository reservationDetailRepository,
      ReservationRepository reservationRepository,
      ToolRepository toolRepository,
      PaymentRepository paymentRepository) {
    this.reservationDetailRepository = reservationDetailRepository;
    this.reservationRepository = reservationRepository;
    this.toolRepository = toolRepository;
    this.paymentRepository = paymentRepository;
  }

  public ReservationDetail execute(Long detailId, int newQuantity) {
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
        throw new RuntimeException("No se puede editar detalles de una reserva " + statusName);
      }
    }

    if (newQuantity < 1) {
      throw new RuntimeException("La cantidad debe ser mayor a 0");
    }

    Tool tool = detail.getTool();
    if (tool == null || tool.getId() == null) {
      throw new RuntimeException("El detalle no tiene herramienta asociada");
    }

    Tool freshTool = toolRepository.findById(tool.getId())
        .orElseThrow(() -> new RuntimeException("Herramienta no encontrada"));

    validateAvailabilityForDates(freshTool, reservation.getStartDate(), reservation.getEndDate(),
        detail.getQuantity(), newQuantity);

    if (detail.getDailyPrice() == null) {
      throw new RuntimeException("El precio diario no puede ser nulo");
    }
    if (detail.getRentalDay() < 1) {
      throw new RuntimeException("Los dias de alquiler deben ser mayores a 0");
    }

    BigDecimal newSubTotal =
        BigDecimal.valueOf(detail.getDailyPrice())
            .multiply(BigDecimal.valueOf(detail.getRentalDay()))
            .multiply(BigDecimal.valueOf(newQuantity));

    ReservationDetail updated = ReservationDetail.reconstruct(
        detail.getId(),
        detail.getTool(),
        detail.getReservation(),
        detail.getDailyPrice(),
        detail.getRentalDay(),
        newQuantity,
        newSubTotal
    );

    ReservationDetail saved = reservationDetailRepository.save(updated);

    adjustToolAvailability(freshTool, detail.getQuantity(), newQuantity);
    updateReservationAndPaymentTotal(reservation);

    return saved;
  }

  private void validateAvailabilityForDates(
      Tool tool,
      LocalDate startDate,
      LocalDate endDate,
      int currentQuantity,
      int newQuantity) {
    int totalQuantity = tool.getTotalQuantity() != null
        ? tool.getTotalQuantity()
        : tool.getAvailableQuantity() != null ? tool.getAvailableQuantity() : 0;

    int reserved = reservationDetailRepository.sumReservedQuantityForToolBetweenDates(
        tool.getId(),
        startDate,
        endDate,
        List.of("cancelled", "finished"));

    int availableByDate = totalQuantity - reserved + currentQuantity;
    if (newQuantity > availableByDate) {
      throw new RuntimeException("La cantidad solicitada excede la disponibilidad para las fechas seleccionadas");
    }
  }

  private void adjustToolAvailability(Tool tool, int oldQuantity, int newQuantity) {
    int delta = newQuantity - oldQuantity;
    if (delta == 0) {
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

    int updated = available - delta;
    if (updated < 0) {
      throw new RuntimeException("La cantidad disponible no puede ser negativa");
    }

    tool.setAvailableQuantity(updated);
    toolRepository.update(tool.getId(), tool);
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
}
