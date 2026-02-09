package com.rentaherramientas.tolly.application.dto.reservationdetail;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Datos para agregar una herramienta a una reserva")
public record CreateReservationDetailRequest(
    @NotNull(message = "reservationId es requerido") @Schema(description = "ID de la reserva", example = "1") Long reservationId,

    @NotNull(message = "toolId es requerido") @Schema(description = "ID de la herramienta", example = "1") Long toolId,

    @Min(value = 1, message = "quantity debe ser mayor o igual a 1") @Schema(description = "Cantidad a reservar", example = "1") Integer quantity) {
}
