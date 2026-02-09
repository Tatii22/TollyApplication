package com.rentaherramientas.tolly.application.dto.returns;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detalle de herramienta devuelta")
public record ReturnDetailResponse(
    @Schema(description = "ID de la herramienta", example = "1")
    Long toolId,

    @Schema(description = "Nombre de la herramienta", example = "Martillo")
    String toolName,

    @Schema(description = "Cantidad devuelta", example = "1")
    Integer quantity,

    @Schema(description = "Observaciones del cliente", example = "Rayones en la carcasa")
    String observations
) {}
