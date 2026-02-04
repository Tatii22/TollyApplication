package com.rentaherramientas.tolly.application.dto.tool;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para crear un nuevo estado de herramienta
 * Solo los ADMIN pueden crear estados
 */
public record CreateToolStatusRequest(
    @NotBlank(message = "El nombre del estado es requerido")
    @Schema(description = "Nombre del estado", example = "DISPONIBLE", requiredMode = Schema.RequiredMode.REQUIRED)
    String name
) {}
