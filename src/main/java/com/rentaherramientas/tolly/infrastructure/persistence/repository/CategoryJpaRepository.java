package com.rentaherramientas.tolly.infrastructure.persistence.repository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByName(String name);
    boolean existsByName(String name);
}
