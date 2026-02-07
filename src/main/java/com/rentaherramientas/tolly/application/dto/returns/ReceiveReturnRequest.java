package com.rentaherramientas.tolly.application.dto.returns;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "Datos para registrar recepcion de devolucion")
public record ReceiveReturnRequest(
    @NotBlank(message = "El estado de devolucion es obligatorio")
    @Schema(description = "Estado de devolucion", example = "RECEIVED")
    String returnStatusName,

    @NotEmpty(message = "Debe incluir al menos un detalle de devolucion")
    @Valid
    @Schema(description = "Detalle de herramientas devueltas", example = "[{\"toolId\":1,\"quantity\":2,\"observations\":\"Golpes menores\"}]")
    List<ReturnDetailRequest> details,

    @Schema(description = "Observaciones generales del proveedor", example = "Equipo recibido con golpes")
    String observations
) {}
