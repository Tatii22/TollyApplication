package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationDetailEntity;

@Repository
public interface ReservationDetailJpaRepository
        extends JpaRepository<ReservationDetailEntity, Long> {


  List<ReservationDetailEntity> findByReservation_Id(Long reservationId);

  // Detalles por herramienta
  List<ReservationDetailEntity> findByTool_Id(Long toolId);

  Optional<ReservationDetailEntity> findById(Long id);

  // Eliminar todos los detalles de una reserva
  void deleteByReservation_Id(Long reservationId);
  boolean existsByReservation_IdAndTool_Id(Long reservationId, Long toolId);


}

