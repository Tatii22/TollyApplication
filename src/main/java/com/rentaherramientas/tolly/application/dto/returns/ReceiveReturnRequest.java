package com.rentaherramientas.tolly.application.dto.returns;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Datos para registrar recepcion de devolucion")
public record ReceiveReturnRequest(
    @NotBlank(message = "El estado de devolucion es obligatorio")
    @Schema(description = "Estado de devolucion", example = "RECEIVED")
    String returnStatusName,

    @Valid
    @Schema(description = "Detalle de herramientas devueltas (opcional, si no se envía se toma de la devolución)", example = "[{\"toolId\":1,\"quantity\":2,\"observations\":\"Golpes menores\"}]")
    List<ReturnDetailRequest> details,

    @Schema(description = "Observaciones generales del proveedor", example = "Equipo recibido con golpes")
    String observations
) {}
