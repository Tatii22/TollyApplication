package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.ToolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolJpaRepository extends JpaRepository<ToolEntity, Long> {
    Optional<ToolEntity> findByName(String name);
    boolean existsByName(String name);
    List<ToolEntity> findByToolStatus(
        String name,
        Integer availableQuantity);

    @Query("SELECT t FROM ToolEntity t WHERE t.category.id = :categoryId")
    List<ToolEntity> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT t FROM ToolEntity t WHERE t.category.id = :categoryId AND t.toolStatus.name = :statusName AND t.availableQuantity > :minAvailableQuantity")
    List<ToolEntity> findByCategoryIdAndStatusName(
        @Param("categoryId") Long categoryId,
        @Param("statusName") String statusName,
        @Param("minAvailableQuantity") Integer minAvailableQuantity);
}
