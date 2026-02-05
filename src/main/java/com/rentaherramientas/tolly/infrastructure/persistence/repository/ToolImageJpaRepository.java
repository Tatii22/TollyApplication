package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.ToolEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ToolImageEntity;

@Repository
public interface ToolImageJpaRepository extends JpaRepository<ToolImageEntity, Long> {
    List<ToolImageEntity> findByTool(ToolEntity tool);
}
