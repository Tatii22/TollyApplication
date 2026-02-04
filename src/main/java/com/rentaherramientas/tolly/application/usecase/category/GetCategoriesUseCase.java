package com.rentaherramientas.tolly.application.usecase.category;

import com.rentaherramientas.tolly.application.dto.category.CategoryResponse;
import com.rentaherramientas.tolly.application.mapper.CategoryMapper;
import com.rentaherramientas.tolly.domain.model.Category;
import com.rentaherramientas.tolly.domain.ports.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UseCase para listar todas las categorías
 */
@Service
public class GetCategoriesUseCase {
    
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    
    public GetCategoriesUseCase(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }
    
    public List<CategoryResponse> execute() {
        return categoryRepository.findAll().stream()
            .map(categoryMapper::toCategoryResponse)
            .collect(Collectors.toList());
    }
}
