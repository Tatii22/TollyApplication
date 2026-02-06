package com.rentaherramientas.tolly.application.dto.returns;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Datos para actualizar una devolucion")
public record UpdateReturnRequest(
    @NotNull(message = "El ID de la reserva es obligatorio")
    @Schema(description = "ID de la reserva", example = "1")
    Long reservationId,

    @NotNull(message = "El ID del cliente es obligatorio")
    @Schema(description = "ID del cliente", example = "1")
    Long clientId,

    @NotNull(message = "La fecha de devolucion es obligatoria")
    @Schema(description = "Fecha de devolucion", example = "2026-02-10")
    LocalDate returnDate,

    @NotNull(message = "El estado de devolucion es obligatorio")
    @Schema(description = "ID del estado de devolucion", example = "2")
    Long returnStatusId,

    @Schema(description = "Observaciones", example = "Devolucion con observaciones menores")
    String observations
) {}
