package com.rentaherramientas.tolly.application.usecase.category;

import com.rentaherramientas.tolly.application.dto.category.CategoryResponse;
import com.rentaherramientas.tolly.application.dto.category.UpdateCategoryRequest;
import com.rentaherramientas.tolly.application.mapper.CategoryMapper;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Category;
import com.rentaherramientas.tolly.domain.ports.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UseCase para actualizar una categoría
 */
@Service
public class UpdateCategoryUseCase {
    
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    
    public UpdateCategoryUseCase(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }
    
    @Transactional
    public CategoryResponse execute(Long id, UpdateCategoryRequest request) {
        // Verificar que la categoría existe
        Category existing = categoryRepository.findById(id)
            .orElseThrow(() -> new DomainException("Categoría con ID " + id + " no encontrada"));
        
        // Verificar que el nuevo nombre no existe en otra categoría
        categoryRepository.findByName(request.name())
            .ifPresent(category -> {
                if (!category.getId().equals(id)) {
                    throw new DomainException("Ya existe otra categoría con el nombre: " + request.name());
                }
            });
        
        // Actualizar la categoría
        Category updatedCategory = new Category(id, request.name());
        Category saved = categoryRepository.save(updatedCategory);
        
        return categoryMapper.toCategoryResponse(saved);
    }
}
