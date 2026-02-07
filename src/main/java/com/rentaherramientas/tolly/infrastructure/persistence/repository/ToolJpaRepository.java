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

    @Query("""
        SELECT t FROM ToolEntity t
        WHERE (:categoryId IS NULL OR t.category.id = :categoryId)
          AND (:statusName IS NULL OR t.toolStatus.name = :statusName)
          AND (:minAvailableQuantity IS NULL OR t.availableQuantity > :minAvailableQuantity)
          AND (:minPrice IS NULL OR t.dailyPrice >= :minPrice)
          AND (:maxPrice IS NULL OR t.dailyPrice <= :maxPrice)
        """)
    List<ToolEntity> findByFilters(
        @Param("categoryId") Long categoryId,
        @Param("statusName") String statusName,
        @Param("minAvailableQuantity") Integer minAvailableQuantity,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice);
}
