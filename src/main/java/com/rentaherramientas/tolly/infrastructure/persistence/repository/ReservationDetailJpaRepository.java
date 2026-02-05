package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationDetailEntity;

@Repository
public interface ReservationDetailJpaRepository
        extends JpaRepository<ReservationDetailEntity, Long> {

  /* Detalles de una reserva
  List<ReservationDetailEntity> findByReservation_Id(Long reservationId);

  // Detalles por herramienta
  List<ReservationDetailEntity> findByTool_Id(Long toolId);

  // Eliminar todos los detalles de una reserva
  void deleteByReservation_Id(Long reservationId);

  */ 
}

