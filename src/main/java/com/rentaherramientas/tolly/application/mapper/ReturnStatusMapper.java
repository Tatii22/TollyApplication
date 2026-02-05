package com.rentaherramientas.tolly.application.mapper;

import org.springframework.stereotype.Component;

import com.rentaherramientas.tolly.domain.model.ReturnStatus;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReturnStatusEntity;

@Component
public class ReturnStatusMapper {

    public static ReturnStatus toDomain(ReturnStatusEntity entity) {
        if (entity == null) return null;
        return new ReturnStatus(entity.getId(), entity.getName());
    }

    public static ReturnStatusEntity toEntity(ReturnStatus domain) {
        if (domain == null) return null;
        ReturnStatusEntity entity = new ReturnStatusEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        return entity;
    }
}
