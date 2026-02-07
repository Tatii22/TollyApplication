package com.rentaherramientas.tolly.application.mapper;

import com.rentaherramientas.tolly.domain.model.ReservationStatus;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationStatusEntity;

import org.springframework.stereotype.Component;

@Component
public class ReservationStatusMapper {

  public static ReservationStatus toDomain(ReservationStatusEntity entity) {
    if (entity == null)
      return null;
    return ReservationStatus.reconstruct(
        entity.getId(),
        entity.getStatusName());
  }

  public static ReservationStatusEntity toEntity(ReservationStatus domain) {
    if (domain == null)
      return null;
    ReservationStatusEntity entity = new ReservationStatusEntity();
    entity.setId(domain.getId());
    entity.setStatusName(domain.getName());
    return entity;
  }
}
