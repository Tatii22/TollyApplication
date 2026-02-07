package com.rentaherramientas.tolly.application.mapper;

import com.rentaherramientas.tolly.domain.model.Return;
import com.rentaherramientas.tolly.domain.model.ReturnDetail;
import com.rentaherramientas.tolly.domain.model.ReturnStatus;
import com.rentaherramientas.tolly.domain.model.Tool;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReturnDetailEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReturnEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ToolEntity;

public class ReturnDetailMapper {

    private ReturnDetailMapper() {
    }

    public static ReturnDetail toDomain(ReturnDetailEntity entity) {
        if (entity == null) return null;

        return ReturnDetail.reconstruct(
            entity.getId(),
            toReturnDomain(entity.getReturnValue()),
            toToolDomain(entity.getTool()),
            entity.getQuantity(),
            entity.getObservations()
        );
    }

    public static ReturnDetailEntity toEntity(ReturnDetail domain) {
        if (domain == null) return null;

        ReturnDetailEntity entity = new ReturnDetailEntity();
        entity.setId(domain.getId());

        if (domain.getReturnValue() != null) {
            entity.setReturnId(domain.getReturnValue().getId());
        }

        if (domain.getTool() != null) {
            entity.setToolId(domain.getTool().getId());
        }

        entity.setQuantity(domain.getQuantity());
        entity.setObservations(domain.getObservations());

        return entity;
    }

    private static Tool toToolDomain(ToolEntity entity) {
        if (entity == null) return null;

        return Tool.reconstruct(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getDailyPrice(),
            entity.getTotalQuantity(),
            entity.getAvailableQuantity(),
            entity.getToolStatus() != null ? entity.getToolStatus().getId() : null,
            entity.getSupplier() != null ? entity.getSupplier().getId() : null,
            entity.getCategory() != null ? entity.getCategory().getId() : null
        );
    }

    private static Return toReturnDomain(ReturnEntity entity) {
        if (entity == null) return null;

        ReturnStatus status = entity.getReturnStatus() != null
            ? new ReturnStatus(entity.getReturnStatus().getId(), entity.getReturnStatus().getName())
            : null;

        return Return.reconstruct(
            entity.getId(),
            entity.getReservationId(),
            entity.getClientId(),
            entity.getReturnDate(),
            status,
            entity.getObservations()
        );
    }
}
