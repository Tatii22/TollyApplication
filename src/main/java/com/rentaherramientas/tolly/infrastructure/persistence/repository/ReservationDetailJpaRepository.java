package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

  @Query("""
      SELECT COALESCE(SUM(d.quantity), 0) FROM ReservationDetailEntity d
      JOIN d.reservation r
      WHERE d.tool.id = :toolId
        AND r.startDate <= :endDate
        AND r.endDate >= :startDate
        AND (LOWER(r.reservationStatus.statusName) NOT IN :excludedStatuses)
      """)
  Integer sumReservedQuantityForToolBetweenDates(
      @Param("toolId") Long toolId,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate,
      @Param("excludedStatuses") Collection<String> excludedStatuses);


}

