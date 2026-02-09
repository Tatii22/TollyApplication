package com.rentaherramientas.tolly.application.usecase.reservationdetail;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.ReservationDetail;
import com.rentaherramientas.tolly.domain.model.Payment;
import com.rentaherramientas.tolly.domain.ports.PaymentRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationDetailRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;

@Service
@Transactional
public class UpdateReservationDetailUseCase {

  private final ReservationDetailRepository reservationDetailRepository;
  private final ReservationRepository reservationRepository;
  private final PaymentRepository paymentRepository;

  public UpdateReservationDetailUseCase(
      ReservationDetailRepository reservationDetailRepository,
      ReservationRepository reservationRepository,
      PaymentRepository paymentRepository) {
    this.reservationDetailRepository = reservationDetailRepository;
    this.reservationRepository = reservationRepository;
    this.paymentRepository = paymentRepository;
  }

  public ReservationDetail execute(
      Long detailId,
      int newRentalDays) {

    ReservationDetail existingDetail =
        reservationDetailRepository.findById(detailId)
            .orElseThrow(() -> new RuntimeException("Detalle de reserva no encontrado"));

    if (existingDetail.getReservation() == null || existingDetail.getReservation().getId() == null) {
      throw new RuntimeException("El detalle no tiene reserva asociada");
    }
    Reservation reservation = reservationRepository.findById(existingDetail.getReservation().getId())
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

    if (newRentalDays < 1) {
      throw new RuntimeException("Los dias de alquiler deben ser mayores a 0");
    }

    if (existingDetail.getDailyPrice() == null) {
      throw new RuntimeException("El precio diario no puede ser nulo");
    }

    BigDecimal newSubTotal =
        BigDecimal.valueOf(existingDetail.getDailyPrice())
            .multiply(BigDecimal.valueOf(newRentalDays))
            .multiply(BigDecimal.valueOf(existingDetail.getQuantity()));

    ReservationDetail updatedDetail =
        ReservationDetail.reconstruct(
            existingDetail.getId(),
            existingDetail.getTool(),
            existingDetail.getReservation(),
            existingDetail.getDailyPrice(),
            newRentalDays,
            existingDetail.getQuantity(),
            newSubTotal
        );

    ReservationDetail saved = reservationDetailRepository.save(updatedDetail);

    updateReservationAndPaymentTotal(reservation);

    return saved;
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
