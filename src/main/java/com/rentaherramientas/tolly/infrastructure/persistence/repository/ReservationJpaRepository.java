package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import java.util.List;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationEntity;

@Repository
public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {

   // Listar por cliente
    List<ReservationEntity> findByClientId(UUID clientId);

    // Listar por algún otro criterio, como fecha (si startDate es LocalDate)
    List<ReservationEntity> findByStartDate(java.time.LocalDate date);


}
