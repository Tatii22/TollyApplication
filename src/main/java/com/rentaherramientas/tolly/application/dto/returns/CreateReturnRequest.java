package com.rentaherramientas.tolly.application.dto.returns;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Datos para crear una devolucion")
public record CreateReturnRequest(
    @NotNull(message = "El ID de la reserva es obligatorio")
    @Schema(description = "ID de la reserva", example = "1")
    Long reservationId,

    @NotNull(message = "La fecha de devolucion es obligatoria")
    @Schema(description = "Fecha de devolucion", example = "2026-02-10")
    LocalDate returnDate,

    @Schema(description = "Estado de devolucion reportado por el cliente", example = "CL_DAMAGED")
    String returnStatusName,

    @Schema(description = "ID del estado de devolucion (se asigna automaticamente a PENDIENTE_DEVOLUCION)", example = "1")
    Long returnStatusId,

    @NotEmpty(message = "Debe incluir al menos un detalle de devolucion")
    @Valid
    @Schema(description = "Detalle de herramientas a devolver", example = "[{\"toolId\":1,\"quantity\":2,\"observations\":\"Sin novedades\"}]")
    List<ReturnDetailRequest> details,

    @Schema(description = "Observaciones", example = "Entrega completa sin novedades")
    String observations
) {}
