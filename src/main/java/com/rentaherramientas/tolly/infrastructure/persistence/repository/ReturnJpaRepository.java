package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReturnEntity;

@Repository
public interface ReturnJpaRepository extends JpaRepository<ReturnEntity, Long> {
}
