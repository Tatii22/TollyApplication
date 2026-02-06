package com.rentaherramientas.tolly.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Payment {
    private final Long id;
    private final Long reservationId;
    private final BigDecimal amount;
    private final LocalDateTime paymentDate;
    private final PaymentStatus status;

    public Payment(Long id, Long reservationId, BigDecimal amount, LocalDateTime paymentDate, PaymentStatus status) {
        if (reservationId == null) {
            throw new IllegalArgumentException("Reservation ID cannot be null");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be a non-negative BigDecimal");
        }
        if (status == null) {
            throw new IllegalArgumentException("Payment status cannot be null");
        }
        this.id = id;
        this.reservationId = reservationId;
        this.amount = amount;
        this.paymentDate = paymentDate != null ? paymentDate : LocalDateTime.now();
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getReservationId() {
        return reservationId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payment)) return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
