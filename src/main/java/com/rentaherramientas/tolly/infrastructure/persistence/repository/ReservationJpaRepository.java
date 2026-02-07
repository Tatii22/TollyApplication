package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import java.util.List;
import java.time.LocalDate;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationEntity;

@Repository
public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {

   // Listar por cliente
    List<ReservationEntity> findByClient_Id(Long clientId);

    // Listar por algún otro criterio, como fecha (si startDate es LocalDate)
    List<ReservationEntity> findByStartDate(java.time.LocalDate date);

    @Query("""
        SELECT r FROM ReservationEntity r
        WHERE (:from IS NULL OR r.startDate >= :from)
          AND (:to IS NULL OR r.startDate <= :to)
        """)
    List<ReservationEntity> findByStartDateRange(
        @Param("from") LocalDate from,
        @Param("to") LocalDate to);

    @Query("""
        SELECT r.client.id, r.client.firstName, r.client.lastName, COUNT(r)
        FROM ReservationEntity r
        WHERE (:from IS NULL OR r.startDate >= :from)
          AND (:to IS NULL OR r.startDate <= :to)
        GROUP BY r.client.id, r.client.firstName, r.client.lastName
        ORDER BY COUNT(r) DESC
        """)
    List<Object[]> findFrequentClients(
        @Param("from") LocalDate from,
        @Param("to") LocalDate to);


}
