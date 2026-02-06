package com.rentaherramientas.tolly.infrastructure.persistence.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.SupplierEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.UserEntity;

@Repository
public interface SupplierJpaRepository extends JpaRepository <SupplierEntity, Long>{

  Optional<SupplierEntity> findByUserId(UserEntity userId);
}
