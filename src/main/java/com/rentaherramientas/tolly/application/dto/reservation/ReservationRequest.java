package com.rentaherramientas.tolly.application.dto.reservation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Datos para crear una reserva")
public record ReservationRequest(
    @Schema(description = "ID del cliente (se toma del token)", hidden = true) Long clientId,

    @NotNull(message = "La fecha de inicio es requerida") @Schema(description = "Fecha de inicio de la reserva", example = "2026-02-05") LocalDate startDate,

    @NotNull(message = "La fecha de fin es requerida") @Schema(description = "Fecha de fin de la reserva", example = "2026-02-10") LocalDate endDate,

    @NotNull(message = "El precio total es requerido") @Positive(message = "El precio total debe ser mayor a cero") @Schema(description = "Precio total de la reserva", example = "150000.00") BigDecimal totalPrice,

    @Schema(description = "Nombre del estado de la reserva", example = "IN_PROGRESS") String statusName) {
}
