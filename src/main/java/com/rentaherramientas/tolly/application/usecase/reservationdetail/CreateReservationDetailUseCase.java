package com.rentaherramientas.tolly.application.usecase.reservationdetail;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.ReservationDetail;
import com.rentaherramientas.tolly.domain.model.Tool;
import com.rentaherramientas.tolly.domain.ports.ReservationDetailRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ToolRepository;

@Service
@Transactional
public class CreateReservationDetailUseCase {

  private final ReservationDetailRepository reservationDetailRepository;
  private final ReservationRepository reservationRepository;
  private final ToolRepository toolRepository;

  public CreateReservationDetailUseCase(
      ReservationDetailRepository reservationDetailRepository,
      ReservationRepository reservationRepository,
      ToolRepository toolRepository) {

    this.reservationDetailRepository = reservationDetailRepository;
    this.reservationRepository = reservationRepository;
    this.toolRepository = toolRepository;
  }

  public ReservationDetail execute(Long reservationId, Long toolId, int quantity) {

    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

    if (reservation.getStatus() != null) {
      String statusName = reservation.getStatus().getName();
      if ("CANCELLED".equalsIgnoreCase(statusName)
          || "FINISHED".equalsIgnoreCase(statusName)
          || "IN_INCIDENT".equalsIgnoreCase(statusName)) {
        throw new RuntimeException("No se puede agregar detalles a una reserva " + statusName);
      }
    }

    Tool tool = toolRepository.findById(toolId)
        .orElseThrow(() -> new RuntimeException("Herramienta no encontrada"));

    if (quantity < 1) {
      throw new RuntimeException("La cantidad debe ser mayor a 0");
    }
    if (tool.getTotalQuantity() == null && tool.getAvailableQuantity() == null) {
      throw new RuntimeException("La herramienta no tiene cantidad total definida");
    }

    long rentalDays = ChronoUnit.DAYS.between(
        reservation.getStartDate(),
        reservation.getEndDate());

    if (rentalDays <= 0) {
      throw new RuntimeException("La fecha de fin debe ser mayor a la fecha de inicio");
    }

    if (rentalDays > Integer.MAX_VALUE) {
      throw new RuntimeException("El numero de dias es demasiado grande");
    }

    int rentalDaysInt = (int) rentalDays;
    Double dailyPrice = tool.getDailyPrice();

    int totalQuantity = tool.getTotalQuantity() != null
        ? tool.getTotalQuantity()
        : tool.getAvailableQuantity();
    int reserved = reservationDetailRepository.sumReservedQuantityForToolBetweenDates(
        toolId,
        reservation.getStartDate(),
        reservation.getEndDate(),
        List.of("cancelled", "finished"));
    int availableByDate = totalQuantity - reserved;
    if (quantity > availableByDate) {
      throw new RuntimeException("La cantidad solicitada excede la disponibilidad para las fechas seleccionadas");
    }

    ReservationDetail detail = ReservationDetail.create(
        tool,
        reservation,
        dailyPrice,
        rentalDaysInt,
        quantity,
        null
    );

    ReservationDetail saved = reservationDetailRepository.save(detail);

    return saved;
  }
}
