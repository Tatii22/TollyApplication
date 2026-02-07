package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReturnDetailEntity;

@Repository
public interface ReturnDetailJpaRepository
    extends JpaRepository<ReturnDetailEntity, Long> {

    List<ReturnDetailEntity> findByReturnValue_Id(Long returnId);

    List<ReturnDetailEntity> findByTool_Id(Long toolId);

    Optional<ReturnDetailEntity> findById(Long id);

    void deleteByReturnValue_Id(Long returnId);

    boolean existsByReturnValue_IdAndTool_Id(Long returnId, Long toolId);
}
