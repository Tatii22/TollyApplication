package com.rentaherramientas.tolly.application.usecase.payment;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Client;
import com.rentaherramientas.tolly.domain.model.Payment;
import com.rentaherramientas.tolly.domain.model.PaymentStatus;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.domain.ports.PaymentRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationStatusRepository;
import com.rentaherramientas.tolly.application.usecase.invoice.GenerateInvoiceForPaymentUseCase;

@Service
public class PayPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final ReservationStatusRepository reservationStatusRepository;
    private final GenerateInvoiceForPaymentUseCase generateInvoiceForPaymentUseCase;

    public PayPaymentUseCase(PaymentRepository paymentRepository,
                             ReservationRepository reservationRepository,
                             ClientRepository clientRepository,
                             ReservationStatusRepository reservationStatusRepository,
                             GenerateInvoiceForPaymentUseCase generateInvoiceForPaymentUseCase) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
        this.reservationStatusRepository = reservationStatusRepository;
        this.generateInvoiceForPaymentUseCase = generateInvoiceForPaymentUseCase;
    }

    @Transactional
    public Payment execute(Long reservationId, UUID userId) {
        if (reservationId == null) {
            throw new DomainException("ReservationId is required");
        }
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

        Payment payment = paymentRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new DomainException("Payment not found for reservation: " + reservationId));

        if (payment.isPaid()) {
            throw new DomainException("Payment already PAID");
        }
        if (!payment.isPending()) {
            throw new DomainException("Payment must be PENDING");
        }

        if (reservation.getTotal() == null || reservation.getTotal().compareTo(payment.getAmount()) != 0) {
            throw new DomainException("Payment amount does not match reservation total");
        }

        Payment updated = new Payment(
                payment.getId(),
                reservation,
                payment.getAmount(),
                LocalDateTime.now(),
                new PaymentStatus(null, PaymentStatus.PAID)
        );

        Payment savedPayment = paymentRepository.save(updated);

        reservationStatusRepository.findByStatusName("IN_PROGRESS")
                .ifPresent(status -> {
                    reservation.setStatus(status);
                    reservationRepository.save(reservation);
                });

        generateInvoiceForPaymentUseCase.execute(savedPayment);

        return savedPayment;
    }
}
