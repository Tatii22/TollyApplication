package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationStatusEntity;

@Repository
public interface ReservationStatusJpaRespository extends JpaRepository<ReservationStatusEntity, UUID> {

  // Buscar por nombre
   Optional<ReservationStatusEntity> findByStatusName(String statusName);


    // Buscar por ID
    Optional<ReservationStatusEntity> findById(UUID id);

}
