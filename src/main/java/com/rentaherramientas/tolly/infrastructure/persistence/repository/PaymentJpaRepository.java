package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.PaymentEntity;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {

    Optional<PaymentEntity> findByReservation_Id(Long reservationId);

    boolean existsByReservation_Id(Long reservationId);

    List<PaymentEntity> findByReservation_Client_Id(Long clientId);

    List<PaymentEntity> findByStatus_NameIgnoreCase(String name);

    @Query("""
        SELECT DISTINCT p FROM PaymentEntity p
        JOIN p.reservation r
        JOIN ReservationDetailEntity d ON d.reservation = r
        JOIN d.tool t
        JOIN t.supplier s
        WHERE s.id = :supplierId
          AND (:from IS NULL OR p.paymentDate >= :from)
          AND (:to IS NULL OR p.paymentDate <= :to)
        """)
    List<PaymentEntity> findBySupplierIdAndDateRange(
        @Param("supplierId") Long supplierId,
        @Param("from") LocalDateTime from,
        @Param("to") LocalDateTime to);

    @Query("""
        SELECT p FROM PaymentEntity p
        WHERE (:from IS NULL OR p.paymentDate >= :from)
          AND (:to IS NULL OR p.paymentDate <= :to)
          AND (:statusName IS NULL OR LOWER(p.status.name) = LOWER(:statusName))
        """)
    List<PaymentEntity> findByDateRange(
        @Param("from") LocalDateTime from,
        @Param("to") LocalDateTime to,
        @Param("statusName") String statusName);

    @Query("""
        SELECT COALESCE(SUM(p.amount), 0) FROM PaymentEntity p
        WHERE LOWER(p.status.name) = 'paid'
          AND (:from IS NULL OR p.paymentDate >= :from)
          AND (:to IS NULL OR p.paymentDate <= :to)
        """)
    BigDecimal sumPaidAmountBetweenDates(
        @Param("from") LocalDateTime from,
        @Param("to") LocalDateTime to);
}
