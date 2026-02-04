package com.rentaherramientas.tolly.application.dto.tool;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de respuesta para un estado de herramienta
 */
public record ToolStatusResponse(
    @Schema(description = "ID del estado", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,
    
    @Schema(description = "Nombre del estado", example = "DISPONIBLE", requiredMode = Schema.RequiredMode.REQUIRED)
    String name
) {}
