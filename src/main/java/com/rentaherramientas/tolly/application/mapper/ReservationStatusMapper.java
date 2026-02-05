package com.rentaherramientas.tolly.application.mapper;

import com.rentaherramientas.tolly.domain.model.ReservationStatus;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationStatusEntity;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class ReservationStatusMapper {

  public static ReservationStatus toDomain(ReservationStatusEntity entity) {
    if (entity == null)
      return null;
    return ReservationStatus.reconstruct(
        UUID.nameUUIDFromBytes(entity.getId().toString().getBytes()), // Convert Long → UUID (aprox.)
        entity.getStatusName());
  }

  public static ReservationStatusEntity toEntity(ReservationStatus domain) {
    if (domain == null)
      return null;
    ReservationStatusEntity entity = new ReservationStatusEntity();
    entity.setId(domain.getId()); // UUID directo
    entity.setStatusName(domain.getName());
    return entity;
  }
}
