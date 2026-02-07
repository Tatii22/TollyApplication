package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.InvoiceEntity;

public interface InvoiceJpaRepository extends JpaRepository<InvoiceEntity, Long> {

  Optional<InvoiceEntity> findByPayment_Id(Long paymentId);

  boolean existsByPayment_Id(Long paymentId);

  List<InvoiceEntity> findByPayment_Reservation_Client_Id(Long clientId);

  @Query("select distinct i from InvoiceEntity i join i.details d where d.tool.supplier.id = :supplierId")
  List<InvoiceEntity> findBySupplierId(@Param("supplierId") Long supplierId);

  @Query("""
      select distinct i from InvoiceEntity i
      join i.payment p
      join p.status ps
      where (:status is null or lower(ps.name) = lower(:status))
        and (:from is null or i.issueDate >= :from)
        and (:to is null or i.issueDate <= :to)
      """)
  List<InvoiceEntity> searchAll(@Param("from") LocalDateTime from,
                                @Param("to") LocalDateTime to,
                                @Param("status") String status);

  @Query("""
      select distinct i from InvoiceEntity i
      join i.payment p
      join p.status ps
      join p.reservation r
      join r.client c
      where c.id = :clientId
        and (:status is null or lower(ps.name) = lower(:status))
        and (:from is null or i.issueDate >= :from)
        and (:to is null or i.issueDate <= :to)
      """)
  List<InvoiceEntity> searchByClientId(@Param("clientId") Long clientId,
                                       @Param("from") LocalDateTime from,
                                       @Param("to") LocalDateTime to,
                                       @Param("status") String status);

  @Query("""
      select distinct i from InvoiceEntity i
      join i.details d
      join d.tool t
      join t.supplier sup
      join i.payment p
      join p.status ps
      where sup.id = :supplierId
        and (:status is null or lower(ps.name) = lower(:status))
        and (:from is null or i.issueDate >= :from)
        and (:to is null or i.issueDate <= :to)
      """)
  List<InvoiceEntity> searchBySupplierId(@Param("supplierId") Long supplierId,
                                         @Param("from") LocalDateTime from,
                                         @Param("to") LocalDateTime to,
                                         @Param("status") String status);
}
