package com.rentaherramientas.tolly.application.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para actualizar una Categoría
 */
@Schema(description = "Request para actualizar una categoría")
public record UpdateCategoryRequest(
    @NotBlank(message = "El nombre de la categoría no puede estar vacío")
    @Schema(description = "Nombre de la categoría", example = "Herramientas Eléctricas")
    String name
) {}
