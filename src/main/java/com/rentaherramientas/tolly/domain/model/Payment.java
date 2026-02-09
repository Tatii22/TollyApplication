package com.rentaherramientas.tolly.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Payment {
    private final Long id;
    private final Reservation reservation;
    private final BigDecimal amount;
    private final LocalDateTime paymentDate;
    private final PaymentStatus status;

    public Payment(Long id, Reservation reservation, BigDecimal amount, LocalDateTime paymentDate, PaymentStatus status) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be a non-negative BigDecimal");
        }
        if (status == null) {
            throw new IllegalArgumentException("Payment status cannot be null");
        }
        this.id = id;
        this.reservation = reservation;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public PaymentStatus getStatus() {
        return status;
    }
    
    public boolean isPaid() {
        return status.isPaid();
    }
    public boolean isPending() {
        return status.isPending();
    }
    public boolean isCancelled() {
        return status.isCancelled();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;
        Payment payment = (Payment) o;
        return id != null && Objects.equals(id, payment.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
