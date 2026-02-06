package com.rentaherramientas.tolly.application.mapper;

import com.rentaherramientas.tolly.application.dto.payment.PaymentResponse;
import com.rentaherramientas.tolly.domain.model.Payment;
import com.rentaherramientas.tolly.domain.model.PaymentStatus;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.PaymentEntity;

public class PaymentMapper {

    public static Payment toDomain(PaymentEntity entity) {
        if (entity == null) return null;

        PaymentStatus status = PaymentStatusMapper.toDomain(entity.getStatus());

        return new Payment(
                entity.getId(),
                ReservationMapper.toDomain(entity.getReservation()),
                entity.getAmount(),
                entity.getPaymentDate(),
                status
        );
    }

    public static PaymentResponse toResponse(Payment payment) {
        if (payment == null) return null;

        return new PaymentResponse(
                payment.getId(),
                payment.getReservation().getId(),
                payment.getAmount(),
                payment.getPaymentDate(),
                payment.getStatus().getName()
        );
    }
}
