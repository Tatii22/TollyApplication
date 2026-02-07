package com.rentaherramientas.tolly.application.usecase.reservation;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.application.dto.reservation.ReservationResponse;
import com.rentaherramientas.tolly.domain.model.Client;
import com.rentaherramientas.tolly.domain.model.Payment;
import com.rentaherramientas.tolly.domain.model.PaymentStatus;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.ReservationStatus;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.domain.ports.PaymentRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationDetailRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationStatusRepository;

@Service
public class ReservationCancelledUseCase {

    private final ReservationRepository reservationRepository;
    private final ReservationStatusRepository reservationStatusRepository;
    private final PaymentRepository paymentRepository;
    private final ClientRepository clientRepository;
    private final ReservationDetailRepository reservationDetailRepository;

    public ReservationCancelledUseCase(ReservationRepository reservationRepository,
                                       ReservationStatusRepository reservationStatusRepository,
                                       PaymentRepository paymentRepository,
                                       ClientRepository clientRepository,
                                       ReservationDetailRepository reservationDetailRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationStatusRepository = reservationStatusRepository;
        this.paymentRepository = paymentRepository;
        this.clientRepository = clientRepository;
        this.reservationDetailRepository = reservationDetailRepository;
    }

    @Transactional
    public ReservationResponse cancelReservation(Long reservationId, UUID userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con ID: " + reservationId));

        Client client = clientRepository.findByUserId(User.restore(userId))
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado para el usuario"));
        if (!reservation.getClientId().equals(client.getId())) {
            throw new IllegalStateException("La reserva no pertenece al cliente");
        }

        if (!"RESERVED".equalsIgnoreCase(reservation.getStatus().getName())) {
            throw new IllegalStateException("Solo se pueden cancelar reservas en estado RESERVED");
        }
        if (!LocalDate.now().isBefore(reservation.getStartDate())) {
            throw new IllegalStateException("Solo se puede cancelar antes de la fecha de inicio");
        }

        reservationDetailRepository.deleteByReservationId(reservation.getId());

        paymentRepository.findByReservationId(reservationId)
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
