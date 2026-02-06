package com.rentaherramientas.tolly.application.mapper;

import com.rentaherramientas.tolly.domain.model.PaymentStatus;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.PaymentStatusEntity;

public class PaymentStatusMapper {

    public static PaymentStatus toDomain(PaymentStatusEntity entity) {
        if (entity == null) return null;

        return new PaymentStatus(
                entity.getId(),
                entity.getName()
        );
    }
}
