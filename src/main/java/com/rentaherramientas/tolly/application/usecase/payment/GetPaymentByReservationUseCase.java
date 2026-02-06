package com.rentaherramientas.tolly.application.usecase.payment;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Client;
import com.rentaherramientas.tolly.domain.model.Payment;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.domain.ports.PaymentRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;

@Service
public class GetPaymentByReservationUseCase {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;

    public GetPaymentByReservationUseCase(PaymentRepository paymentRepository,
                                          ReservationRepository reservationRepository,
                                          ClientRepository clientRepository) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
    }

    public Payment execute(Long reservationId, UUID userId, boolean validateOwner) {
        if (reservationId == null) {
            throw new DomainException("ReservationId is required");
        }

        if (validateOwner) {
            if (userId == null) {
                throw new DomainException("UserId is required");
            }
            Reservation reservation = reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new DomainException("Reservation not found: " + reservationId));
            Client client = clientRepository.findByUserId(User.restore(userId))
                    .orElseThrow(() -> new DomainException("Client not found for user"));
            if (!reservation.getClientId().equals(client.getId())) {
                throw new DomainException("Reservation does not belong to client");
            }
        }

        return paymentRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new DomainException("Payment not found for reservation: " + reservationId));
    }
}
