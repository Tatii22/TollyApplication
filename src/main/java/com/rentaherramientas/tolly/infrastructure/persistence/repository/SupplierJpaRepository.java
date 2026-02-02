package com.rentaherramientas.tolly.infrastructure.persistence.repository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.SupplierEntity;

@Repository
public interface SupplierJpaRepository extends JpaRepository <SupplierEntity, UUID>{

  Optional<SupplierEntity> findByUserId(UUID userId);
}
