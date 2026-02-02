package com.rentaherramientas.tolly.application.usecase.category;

import com.rentaherramientas.tolly.application.dto.category.CategoryResponse;
import com.rentaherramientas.tolly.application.mapper.CategoryMapper;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.ports.CategoryRepository;
import org.springframework.stereotype.Service;

/**
 * UseCase para obtener una categoría por ID
 */
@Service
public class GetCategoryByIdUseCase {
    
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    
    public GetCategoryByIdUseCase(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }
    
    public CategoryResponse execute(Long id) {
        return categoryRepository.findById(id)
            .map(categoryMapper::toCategoryResponse)
            .orElseThrow(() -> new DomainException("Categoría con ID " + id + " no encontrada"));
    }
}
