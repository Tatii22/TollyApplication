package com.rentaherramientas.tolly.application.usecase.category;

import com.rentaherramientas.tolly.application.dto.category.CategoryResponse;
import com.rentaherramientas.tolly.application.dto.category.CreateCategoryRequest;
import com.rentaherramientas.tolly.application.mapper.CategoryMapper;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Category;
import com.rentaherramientas.tolly.domain.ports.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UseCase para crear una nueva categoría
 */
@Service
public class CreateCategoryUseCase {
    
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    
    public CreateCategoryUseCase(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }
    
    @Transactional
    public CategoryResponse execute(CreateCategoryRequest request) {
        // Verificar si la categoría ya existe
        if (categoryRepository.existsByName(request.name())) {
            throw new DomainException("Ya existe una categoría con el nombre: " + request.name());
        }
        
        // Crear la categoría
        Category category = categoryMapper.toCategory(request);
        Category saved = categoryRepository.save(category);
        
        return categoryMapper.toCategoryResponse(saved);
    }
}
