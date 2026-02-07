package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.ToolStatusEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolStatusJpaRepository extends JpaRepository<ToolStatusEntity, Long> {
    Optional<ToolStatusEntity> findByName(String name);
}
