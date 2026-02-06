package com.rentaherramientas.tolly.application.mapper;

import com.rentaherramientas.tolly.domain.model.Payment;
import com.rentaherramientas.tolly.domain.model.PaymentStatus;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.PaymentEntity;

public class PaymentMapper {
    
    public Payment toDomain(PaymentEntity entity) {
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
    
}
