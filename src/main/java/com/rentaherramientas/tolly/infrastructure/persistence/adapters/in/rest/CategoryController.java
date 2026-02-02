package com.rentaherramientas.tolly.infrastructure.persistence.adapters.in.rest;

import com.rentaherramientas.tolly.application.dto.category.CategoryResponse;
import com.rentaherramientas.tolly.application.dto.category.CreateCategoryRequest;
import com.rentaherramientas.tolly.application.dto.category.UpdateCategoryRequest;
import com.rentaherramientas.tolly.application.usecase.category.CreateCategoryUseCase;
import com.rentaherramientas.tolly.application.usecase.category.DeleteCategoryUseCase;
import com.rentaherramientas.tolly.application.usecase.category.GetCategoriesUseCase;
import com.rentaherramientas.tolly.application.usecase.category.GetCategoryByIdUseCase;
import com.rentaherramientas.tolly.application.usecase.category.UpdateCategoryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para gestión de categorías
 * Requiere autenticación para operaciones de creación, actualización y eliminación
 */
@Tag(name = "Gestión de Categorías", description = "Endpoints para administración de categorías de herramientas")
@RestController
@RequestMapping("/categories")
public class CategoryController {
    
    private final GetCategoriesUseCase getCategoriesUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final CreateCategoryUseCase createCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    public CategoryController(
            GetCategoriesUseCase getCategoriesUseCase,
            GetCategoryByIdUseCase getCategoryByIdUseCase,
            CreateCategoryUseCase createCategoryUseCase,
            UpdateCategoryUseCase updateCategoryUseCase,
            DeleteCategoryUseCase deleteCategoryUseCase) {
        this.getCategoriesUseCase = getCategoriesUseCase;
        this.getCategoryByIdUseCase = getCategoryByIdUseCase;
        this.createCategoryUseCase = createCategoryUseCase;
        this.updateCategoryUseCase = updateCategoryUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;
    }

    @Operation(summary = "Listar todas las categorías", description = "Obtiene la lista completa de categorías disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente", 
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll() {
        List<CategoryResponse> categories = getCategoriesUseCase.execute();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Obtener categoría por ID", description = "Obtiene los detalles de una categoría específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría obtenida exitosamente", 
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {
        CategoryResponse category = getCategoryByIdUseCase.execute(id);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Crear nueva categoría", description = "Crea una nueva categoría de herramientas. Requiere rol ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente", 
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o categoría duplicada"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No tiene permisos (requiere ADMIN)")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CreateCategoryRequest request) {
        CategoryResponse category = createCategoryUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @Operation(summary = "Actualizar categoría", description = "Actualiza los datos de una categoría existente. Requiere rol ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente", 
                    content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o nombre duplicado"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No tiene permisos (requiere ADMIN)"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request) {
        CategoryResponse category = updateCategoryUseCase.execute(id, request);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría existente. Requiere rol ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No tiene permisos (requiere ADMIN)"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteCategoryUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}

