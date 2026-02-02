package com.rentaherramientas.tolly.application.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para respuesta de Categoría
 */
@Schema(description = "Categoría de herramientas")
public record CategoryResponse(
    @Schema(description = "ID de la categoría", example = "1")
    Long id,
    
    @Schema(description = "Nombre de la categoría", example = "Herramientas Manuales")
    String name
) {}
