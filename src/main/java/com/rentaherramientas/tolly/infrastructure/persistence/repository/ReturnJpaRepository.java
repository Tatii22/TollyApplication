package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReturnEntity;

@Repository
public interface ReturnJpaRepository extends JpaRepository<ReturnEntity, Long> {
    List<ReturnEntity> findByReservation_Id(Long reservationId);
}
