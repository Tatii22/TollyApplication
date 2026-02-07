package com.rentaherramientas.tolly.application.dto.reservation;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para respuesta de estado de reserva
 */
@Schema(description = "Estado de una reserva")
public record ReservationStatusResponse(

    @Schema(description = "ID del estado de reserva", example = "1")
    Long id,

    @Schema(description = "Nombre del estado de reserva", example = "PAID")
    String statusName

) {}
