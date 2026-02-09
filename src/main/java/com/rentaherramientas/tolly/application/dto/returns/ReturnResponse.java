package com.rentaherramientas.tolly.application.dto.returns;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de una devolucion")
public record ReturnResponse(
    @Schema(description = "ID de la devolucion", example = "1")
    Long id,

    @Schema(description = "ID de la reserva", example = "1")
    Long reservationId,

    @Schema(description = "ID del cliente", example = "1")
    Long clientId,

    @Schema(description = "Fecha de devolucion", example = "2026-02-10")
    LocalDate returnDate,

    @Schema(description = "ID del estado de devolucion", example = "1")
    Long returnStatusId,

    @Schema(description = "Nombre del estado de devolucion", example = "PENDIENTE")
    String returnStatusName,

    @Schema(description = "Observaciones", example = "Entrega completa sin novedades")
    String observations,

    @Schema(description = "Detalles de herramientas devueltas")
    List<ReturnDetailResponse> details
) {}
