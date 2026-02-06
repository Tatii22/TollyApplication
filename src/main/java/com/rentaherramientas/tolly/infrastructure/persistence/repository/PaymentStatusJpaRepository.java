package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.PaymentStatusEntity;

public interface PaymentStatusJpaRepository extends JpaRepository<PaymentStatusEntity, Long> {
    Optional<PaymentStatusEntity> findByNameIgnoreCase(String name);
}
