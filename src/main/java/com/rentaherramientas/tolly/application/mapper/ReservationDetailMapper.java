package com.rentaherramientas.tolly.application.mapper;

import com.rentaherramientas.tolly.domain.model.ReservationDetail;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.Tool;
import com.rentaherramientas.tolly.domain.model.ReservationStatus;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationDetailEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ToolEntity;
import java.math.BigDecimal;

public class ReservationDetailMapper {

  private ReservationDetailMapper() {
  }

  /*
   * =========================
   * ENTITY → DOMAIN
   * =========================
   */
  public static ReservationDetail toDomain(ReservationDetailEntity entity) {
    if (entity == null) return null;

    return ReservationDetail.reconstruct(
        entity.getId(),
        toToolDomain(entity.getTool()),
        toReservationDomain(entity.getReservation()),
        entity.getDailyPrice() != null ? entity.getDailyPrice().doubleValue() : null,
        entity.getRentalDay(),
        entity.getSubTotal()
    );
  }

  /*
   * =========================
   * DOMAIN → ENTITY
   * =========================
   */
  public static ReservationDetailEntity toEntity(ReservationDetail domain) {
    if (domain == null) return null;

    ReservationDetailEntity entity = new ReservationDetailEntity();
    entity.setId(domain.getId());

    if (domain.getTool() != null) {
      entity.setToolId(domain.getTool().getId());
    }

    if (domain.getReservation() != null) {
      entity.setReservationId(domain.getReservation().getId());
    }

    entity.setDailyPrice(
        domain.getDailyPrice() != null ? BigDecimal.valueOf(domain.getDailyPrice()) : null
    );
    entity.setRentalDay(domain.getRentalDay());
    entity.setSubTotal(domain.getSubTotal());

    return entity;
  }

  /*
   * =========================
   * MAPPERS SIMPLES
   * =========================
   */

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

  private static Reservation toReservationDomain(ReservationEntity entity) {
    if (entity == null) return null;

    ReservationStatus status = entity.getReservationStatus() != null
        ? ReservationStatusMapper.toDomain(entity.getReservationStatus())
        : null;

    return Reservation.reconstruct(
        entity.getId(),
        entity.getClient() != null ? entity.getClient().getId() : null,
        entity.getStartDate(),
        entity.getEndDate(),
        entity.getTotalPrice(),
        status,
        entity.getCreatedAt()
    );
  }
}
