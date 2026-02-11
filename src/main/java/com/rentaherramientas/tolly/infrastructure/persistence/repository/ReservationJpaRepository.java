package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationEntity;

@Repository
public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {

   // Listar por cliente
    List<ReservationEntity> findByClient_Id(Long clientId);

    // Listar por algún otro criterio, como fecha (si startDate es LocalDate)
    List<ReservationEntity> findByStartDate(java.time.LocalDate date);

  Optional<ReservationEntity> findById(Long id);

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

    @Query("""
        SELECT DISTINCT r FROM ReservationEntity r
        JOIN ReservationDetailEntity d ON d.reservation.id = r.id
        JOIN d.tool t
        WHERE t.supplier.id = :supplierId
          AND (:statusName IS NULL OR LOWER(r.reservationStatus.statusName) = LOWER(:statusName))
          AND (:from IS NULL OR r.startDate >= :from)
          AND (:to IS NULL OR r.startDate <= :to)
        """)
    List<ReservationEntity> findBySupplierIdAndFilters(
        @Param("supplierId") Long supplierId,
        @Param("statusName") String statusName,
        @Param("from") LocalDate from,
        @Param("to") LocalDate to);

    @Query("""
        SELECT r FROM ReservationEntity r
        WHERE r.client.id = :clientId
          AND (:statusName IS NULL OR LOWER(r.reservationStatus.statusName) = LOWER(:statusName))
          AND (:from IS NULL OR r.startDate >= :from)
          AND (:to IS NULL OR r.startDate <= :to)
        """)
    Page<ReservationEntity> findByClientIdAndFilters(
        @Param("clientId") Long clientId,
        @Param("statusName") String statusName,
        @Param("from") LocalDate from,
        @Param("to") LocalDate to,
        Pageable pageable);

    @Query("""
        SELECT DISTINCT r FROM ReservationEntity r
        JOIN ReservationDetailEntity d ON d.reservation.id = r.id
        JOIN d.tool t
        WHERE t.supplier.id = :supplierId
          AND (:statusName IS NULL OR LOWER(r.reservationStatus.statusName) = LOWER(:statusName))
          AND (:from IS NULL OR r.startDate >= :from)
          AND (:to IS NULL OR r.startDate <= :to)
        """)
    Page<ReservationEntity> findBySupplierIdAndFilters(
        @Param("supplierId") Long supplierId,
        @Param("statusName") String statusName,
        @Param("from") LocalDate from,
        @Param("to") LocalDate to,
        Pageable pageable);


}
