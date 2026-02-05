package com.rentaherramientas.tolly.application.mapper;

import org.springframework.stereotype.Component;

import com.rentaherramientas.tolly.domain.model.Return;
import com.rentaherramientas.tolly.domain.model.ReturnStatus;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ClientEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReturnEntity;

@Component
public class ReturnMapper {

    // De Entity a Dominio
    public static Return toDomain(ReturnEntity entity) {
        if (entity == null) return null;

        ReturnStatus status = ReturnStatusMapper.toDomain(entity.getStatus());

        return new Return(
                entity.getId(),
                entity.getReservationId(),
                entity.getClientId() != null ? entity.getClientId().getId() : null, // UUID del cliente
                entity.getReturnDate(),
                status,
                entity.getObservations()
        );
    }

    // De Dominio a Entity
    public static ReturnEntity toEntity(Return domain, ClientEntity clientEntity) {
        if (domain == null) return null;

        ReturnEntity entity = new ReturnEntity();
        entity.setId(domain.getId());
        entity.setReservationId(domain.getReservationId());
        entity.setClientId(clientEntity); // Se pasa el ClientEntity real
        entity.setReturnDate(domain.getReturnDate());
        entity.setStatus(ReturnStatusMapper.toEntity(domain.getStatus()));
        entity.setObservations(domain.getObservations());

        return entity;
    }
}
