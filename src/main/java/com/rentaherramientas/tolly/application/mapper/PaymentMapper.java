package com.rentaherramientas.tolly.application.mapper;

import org.springframework.stereotype.Component;

import com.rentaherramientas.tolly.domain.model.Payment;
import com.rentaherramientas.tolly.domain.model.PaymentStatus;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.PaymentEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.PaymentStatusEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationEntity;

@Component
public class PaymentMapper {

    // De Entity a Dominio
    public static Payment toDomain(PaymentEntity entity) {
        if (entity == null) return null;

        PaymentStatus status = PaymentStatusMapper.toDomain(entity.getStatus());

        return Payment.reconstruct(
                entity.getId(),
                entity.getReservationId() != null ? entity.getReservationId().getId() : null,
                entity.getAmount(),
                entity.getPaymentDate(),
                status
        );
    }

    // De Dominio a Entity
    public static PaymentEntity toEntity(Payment domain, ReservationEntity reservationEntity) {
        if (domain == null) return null;

        PaymentStatusEntity statusEntity = PaymentStatusMapper.toEntity(domain.getStatus());

        if (domain.getId() != null) {
            // Constructor completo con ID
            return new PaymentEntity(
                    domain.getId(),
                    reservationEntity,
                    domain.getAmount(),
                    domain.getPaymentDate(),
                    statusEntity
            );
        }

        // Constructor sin ID para creación
        return new PaymentEntity(
                reservationEntity,
                domain.getAmount(),
                domain.getPaymentDate(),
                statusEntity
        );
    }
}
