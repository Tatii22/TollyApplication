package com.rentaherramientas.tolly.application.dto.reservation;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Datos para crear una reserva")
public record ReservationRequest(
    @Schema(description = "ID del cliente", example = "550e8400-e29b-41d4-a716-446655440001")
    UUID clientId,

    @Schema(description = "Fecha de inicio de la reserva", example = "2026-02-05")
    LocalDate startDate,

    @Schema(description = "Fecha de fin de la reserva", example = "2026-02-10")
    LocalDate endDate,

    @Schema(description = "Precio total de la reserva", example = "150000.00")
    BigDecimal totalPrice,

    @Schema(description = "Nombre del estado de la reserva", example = "IN_PROGRESS")
    String statusName
) {}
