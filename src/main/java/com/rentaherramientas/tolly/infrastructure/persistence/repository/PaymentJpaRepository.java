package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.PaymentEntity;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {

    Optional<PaymentEntity> findByReservation_Id(Long reservationId);

    boolean existsByReservation_Id(Long reservationId);

    List<PaymentEntity> findByReservation_Client_Id(Long clientId);

    List<PaymentEntity> findByStatus_NameIgnoreCase(String name);
}
