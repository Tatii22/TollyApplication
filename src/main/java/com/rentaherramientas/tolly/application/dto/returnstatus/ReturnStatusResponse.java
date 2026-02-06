package com.rentaherramientas.tolly.application.dto.returnstatus;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de respuesta para un estado de devolucion
 */
public record ReturnStatusResponse(
    @Schema(description = "ID del estado", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,

    @Schema(description = "Nombre del estado", example = "PENDIENTE", requiredMode = Schema.RequiredMode.REQUIRED)
    String name
) {}
