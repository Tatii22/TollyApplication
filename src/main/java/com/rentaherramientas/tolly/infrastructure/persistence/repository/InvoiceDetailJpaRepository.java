package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.InvoiceDetailEntity;

public interface InvoiceDetailJpaRepository extends JpaRepository<InvoiceDetailEntity, Long> {
}
