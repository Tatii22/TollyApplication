package com.rentaherramientas.tolly.application.dto.reservation;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para crear o actualizar un estado de reserva
 */
@Schema(description = "Datos para crear o actualizar un estado de reserva")
public record ReservationStatusRequest(

    @Schema(description = "Nombre del estado de reserva", example = "IN_PROGRESS")
    String statusName

) {}
