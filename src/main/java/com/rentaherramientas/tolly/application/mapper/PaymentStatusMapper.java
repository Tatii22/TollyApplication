package com.rentaherramientas.tolly.application.mapper;

import org.springframework.stereotype.Component;

import com.rentaherramientas.tolly.domain.model.PaymentStatus;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.PaymentStatusEntity;

@Component
public class PaymentStatusMapper {

    // De Entity a Dominio
    public static PaymentStatus toDomain(PaymentStatusEntity entity) {
        if (entity == null) return null;
        return PaymentStatus.reconstruct(entity.getId(), entity.getName());
    }

    // De Dominio a Entity
    public static PaymentStatusEntity toEntity(PaymentStatus domain) {
        if (domain == null) return null;

        if (domain.getId() != null) {
            // Si ya existe en DB
            return new PaymentStatusEntity(domain.getId(), domain.getName());
        }
        return new PaymentStatusEntity(domain.getName());
    }
}

