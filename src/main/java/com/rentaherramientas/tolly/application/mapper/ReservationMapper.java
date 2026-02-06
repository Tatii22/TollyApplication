package com.rentaherramientas.tolly.application.mapper;

import org.springframework.stereotype.Component;

import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.ReservationStatus;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ClientEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationStatusEntity;

@Component
public class ReservationMapper {

    // De Entity a Dominio
    public static Reservation toDomain(ReservationEntity entity) {
        if (entity == null) return null;

        ReservationStatus status = ReservationStatusMapper.toDomain(entity.getReservationStatus());

        return Reservation.reconstruct(
                entity.getId(),
                entity.getClient() != null ? entity.getClient().getId() : null, // ID del cliente
                entity.getStartDate(), // LocalDate directo
                entity.getEndDate(),   // LocalDate directo
                entity.getTotalPrice(),
                status,
                entity.getCreatedAt()  // LocalDate directo
        );
    }

    // De Dominio a Entity
    public static ReservationEntity toEntity(Reservation domain, ClientEntity clientEntity, ReservationStatusEntity statusEntity) {
        if (domain == null) return null;

        ReservationEntity entity = new ReservationEntity();
        entity.setId(domain.getId());
        entity.setClient(clientEntity); // Cliente real
        entity.setStartDate(domain.getStartDate()); // LocalDate directo
        entity.setEndDate(domain.getEndDate());     // LocalDate directo
        entity.setTotalPrice(domain.getTotal());
        entity.setReservationStatus(statusEntity);
        entity.setCreatedAt(domain.getCreatedAt()); // LocalDate directo

        return entity;
    }
}
