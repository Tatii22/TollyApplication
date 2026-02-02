package com.rentaherramientas.tolly.application.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para crear una nueva Categoría
 */
@Schema(description = "Request para crear una categoría")
public record CreateCategoryRequest(
    @NotBlank(message = "El nombre de la categoría no puede estar vacío")
    @Schema(description = "Nombre de la categoría", example = "Herramientas Manuales")
    String name
) {}
