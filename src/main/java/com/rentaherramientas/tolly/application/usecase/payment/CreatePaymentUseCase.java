package com.rentaherramientas.tolly.application.usecase.payment;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.application.dto.payment.CreatePaymentRequest;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Payment;
import com.rentaherramientas.tolly.domain.model.PaymentStatus;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.ports.PaymentRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;

@Service
public class CreatePaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    public CreatePaymentUseCase(PaymentRepository paymentRepository,
                                ReservationRepository reservationRepository) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Payment execute(CreatePaymentRequest request) {

        if (request == null) {
            throw new DomainException("Request cannot be null");
        }

        if (request.getReservationId() == null) {
            throw new DomainException("ReservationId is required");
        }

        Long reservationId = request.getReservationId();

        // 1) Validar reserva existe
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new DomainException("Reservation not found: " + reservationId));

        // 2) Validar estado permitido (esto depende de tu ReservationStatus)
        // Ajusta esta validación según tu modelo
        if (!reservation.getStatus().getName().equalsIgnoreCase("RESERVED")) {
            throw new DomainException("Only RESERVED reservations can generate payments");
        }

        // 3) Evitar duplicados
        if (paymentRepository.existsByReservationId(reservationId)) {
            throw new DomainException("Payment already exists for reservation: " + reservationId);
        }

        // 4) Validar monto (sale del total de la reserva)
        BigDecimal expected = reservation.getTotal();

        if (expected == null || expected.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("Reservation total is invalid. Cannot generate payment.");
        }

        // Si el request trae amount, debe coincidir
        if (request.getAmount() != null && request.getAmount().compareTo(expected) != 0) {
            throw new DomainException("Amount does not match reservation total");
        }

        // 5) Crear pago (lo dejamos PAGADO directo para cumplir el flujo)
        Payment payment = new Payment(
                null,
                reservation,
                expected,
                null,
                new PaymentStatus(null, PaymentStatus.PAGADO)
        );

        return paymentRepository.save(payment);
    }
}
