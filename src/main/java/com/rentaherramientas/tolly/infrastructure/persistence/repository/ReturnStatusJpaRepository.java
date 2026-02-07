package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReturnStatusEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnStatusJpaRepository extends JpaRepository<ReturnStatusEntity, Long> {
    Optional<ReturnStatusEntity> findByName(String name);
}
