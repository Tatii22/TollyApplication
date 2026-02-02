package com.rentaherramientas.tolly.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información del cliente")
public record ClientResponse(
    @Schema(description = "Dirección del cliente", example = "Calle 123 #45-67")
    String address
) {}
