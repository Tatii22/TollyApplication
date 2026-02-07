package com.rentaherramientas.tolly.application.dto.returns;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Detalle de herramienta devuelta")
public record ReturnDetailRequest(
    @NotNull(message = "El ID de la herramienta es obligatorio")
    @Schema(description = "ID de la herramienta", example = "1")
    Long toolId,

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    @Schema(description = "Cantidad devuelta", example = "1")
    Integer quantity,

    @Schema(description = "Observaciones del detalle", example = "Rasguños en la carcasa")
    String observations
) {}
