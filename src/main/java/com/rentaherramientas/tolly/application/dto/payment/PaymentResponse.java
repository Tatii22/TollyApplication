package com.rentaherramientas.tolly.application.dto.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {

    private Long id;
    private Long reservationId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String status;

    public PaymentResponse(Long id, Long reservationId, BigDecimal amount, LocalDateTime paymentDate, String status) {
        this.id = id;
        this.reservationId = reservationId;
        this.amount = amount;
        this.paymentDate = paymentDate;
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

    public String getStatus() {
        return status;
    }
}
