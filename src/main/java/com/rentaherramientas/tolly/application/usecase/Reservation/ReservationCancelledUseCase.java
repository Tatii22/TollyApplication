package com.rentaherramientas.tolly.application.usecase.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.application.dto.reservation.ReservationResponse;
import com.rentaherramientas.tolly.domain.model.Payment;
import com.rentaherramientas.tolly.domain.model.PaymentStatus;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.ReservationStatus;
import com.rentaherramientas.tolly.domain.ports.PaymentRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationStatusRepository;

@Service
public class ReservationCancelledUseCase {

    private final ReservationRepository reservationRepository;
    private final ReservationStatusRepository reservationStatusRepository;
    private final PaymentRepository paymentRepository;

    public ReservationCancelledUseCase(ReservationRepository reservationRepository,
                                       ReservationStatusRepository reservationStatusRepository,
                                       PaymentRepository paymentRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationStatusRepository = reservationStatusRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public ReservationResponse cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con ID: " + reservationId));

        if (!"IN_PROGRESS".equalsIgnoreCase(reservation.getStatus().getName())) {
            throw new IllegalStateException("Solo se pueden cancelar reservas en estado IN_PROGRESS");
        }

        paymentRepository.findByReservationId(reservationId)
                .filter(Payment::isPaid)
                .ifPresent(payment -> paymentRepository.save(new Payment(
                        payment.getId(),
                        reservation,
                        payment.getAmount(),
                        null,
                        new PaymentStatus(null, PaymentStatus.CANCELLED)
                )));

        ReservationStatus cancelledStatus = reservationStatusRepository.findByStatusName("CANCELLED")
                .orElseThrow(() -> new IllegalArgumentException("Estado CANCELLED no encontrado en la base de datos"));
        reservation.setStatus(cancelledStatus);

        Reservation updated = reservationRepository.save(reservation);

        return mapToResponse(updated);
    }

    private ReservationResponse mapToResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getClientId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getTotal(),
                reservation.getStatus().getName(),
                reservation.getCreatedAt()
        );
    }
}
