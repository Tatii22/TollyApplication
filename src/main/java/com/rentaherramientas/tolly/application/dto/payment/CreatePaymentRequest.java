package com.rentaherramientas.tolly.application.dto.payment;

import java.math.BigDecimal;

public class CreatePaymentRequest {

    private Long reservationId;
    private BigDecimal amount; // opcional, el backend lo valida contra reservation.total

    public CreatePaymentRequest() {
    }

    public CreatePaymentRequest(Long reservationId, BigDecimal amount) {
        this.reservationId = reservationId;
        this.amount = amount;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
