package com.rentaherramientas.tolly.application.dto.reservation;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

/**
 * DTO para respuesta de estado de reserva
 */
@Schema(description = "Estado de una reserva")
public record ReservationStatusResponse(

    @Schema(description = "ID del estado de reserva", example = "550e8400-e29b-41d4-a716-446655440001")
    UUID id,

    @Schema(description = "Nombre del estado de reserva", example = "PAID")
    String statusName

) {}
