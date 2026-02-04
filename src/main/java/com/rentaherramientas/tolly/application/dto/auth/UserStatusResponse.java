package com.rentaherramientas.tolly.application.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información del estado del usuario")
public record UserStatusResponse(
    @Schema(description = "ID del estado del usuario", example = "1")
    String id,

    @Schema(description = "Nombre del estado del usuario", example = "ACTIVE")
    String name


) {

}
