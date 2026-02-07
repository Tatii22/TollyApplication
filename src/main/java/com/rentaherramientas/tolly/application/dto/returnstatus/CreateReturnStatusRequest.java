package com.rentaherramientas.tolly.application.dto.returnstatus;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para crear un nuevo estado de devolucion
 * Solo los ADMIN pueden crear estados
 */
public record CreateReturnStatusRequest(
    @NotBlank(message = "El nombre del estado es requerido")
    @Schema(description = "Nombre del estado", example = "PENDIENTE", requiredMode = Schema.RequiredMode.REQUIRED)
    String name
) {}
