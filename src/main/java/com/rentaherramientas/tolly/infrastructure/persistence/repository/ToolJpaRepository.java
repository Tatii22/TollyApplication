package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.ToolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolJpaRepository extends JpaRepository<ToolEntity, Long> {
}
