package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.ToolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToolJpaRepository extends JpaRepository<ToolEntity, Long> {
    Optional<ToolEntity> findByName(String name);
    boolean existsByName(String name);
}
