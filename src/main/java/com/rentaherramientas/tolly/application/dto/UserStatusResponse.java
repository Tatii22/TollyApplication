package com.rentaherramientas.tolly.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estado del usuario")
public record UserStatusResponse(

    @Schema(description = "Nombre del estado", example = "ACTIVE")
    String name

) {}
