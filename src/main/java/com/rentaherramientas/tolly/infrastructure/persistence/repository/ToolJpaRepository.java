package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.ToolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolJpaRepository extends JpaRepository<ToolEntity, Long> {
    Optional<ToolEntity> findByName(String name);
    boolean existsByName(String name);
    List<ToolEntity> findByToolStatus(
        String name,
        Integer availableQuantity);
}
