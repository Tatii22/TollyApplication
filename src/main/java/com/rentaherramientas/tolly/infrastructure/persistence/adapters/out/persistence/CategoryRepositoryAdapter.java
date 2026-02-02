package com.rentaherramientas.tolly.infrastructure.persistence.adapters.out.persistence;

import com.rentaherramientas.tolly.domain.model.Category;
import com.rentaherramientas.tolly.domain.ports.CategoryRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.CategoryEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.CategoryJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CategoryRepositoryAdapter implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;

    public CategoryRepositoryAdapter(CategoryJpaRepository categoryJpaRepository) {
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Override
    public Category save(Category category) {
        CategoryEntity entity = toEntity(category);
        CategoryEntity saved = categoryJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryJpaRepository.findByName(name).map(this::toDomain);
    }

    @Override
    public List<Category> findAll() {
        return categoryJpaRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return categoryJpaRepository.existsByName(name);
    }

    @Override
    public void deleteById(Long id) {
        categoryJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Category> update(Long id, Category category) {
        return categoryJpaRepository.findById(id)
            .map(existing -> {
                existing.setName(category.getName());
                CategoryEntity updated = categoryJpaRepository.save(existing);
                return toDomain(updated);
            });
    }

    private CategoryEntity toEntity(Category category) {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(category.getId());
        entity.setName(category.getName());
        return entity;
    }

    private Category toDomain(CategoryEntity entity) {
        return new Category(entity.getId(), entity.getName());
    }
}
