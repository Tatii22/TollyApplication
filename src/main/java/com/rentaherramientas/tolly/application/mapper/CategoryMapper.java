package com.rentaherramientas.tolly.application.mapper;

import com.rentaherramientas.tolly.application.dto.category.CategoryResponse;
import com.rentaherramientas.tolly.application.dto.category.CreateCategoryRequest;
import com.rentaherramientas.tolly.application.dto.category.UpdateCategoryRequest;
import com.rentaherramientas.tolly.domain.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper MapStruct para conversión entre Category (dominio) y DTOs
 *
 * Con componentModel = "spring", se inyecta como bean de Spring.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper{

    /**
     * Convierte Category de dominio a CategoryResponse (DTO)
     */
    CategoryResponse toCategoryResponse(Category category);

    /**
     * Convierte CreateCategoryRequest (DTO) a Category de dominio
     */
    @Mapping(target = "id", ignore = true)
    Category toCategory(CreateCategoryRequest request);

    /**
     * Convierte UpdateCategoryRequest (DTO) a Category de dominio
     */
    @Mapping(target = "id", ignore = true)
    Category toCategory(UpdateCategoryRequest request);
}
