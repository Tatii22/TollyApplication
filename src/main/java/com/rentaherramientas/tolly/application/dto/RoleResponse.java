package com.rentaherramientas.tolly.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para respuesta de Role
 */
@Schema(description = "Rol del sistema")
public record RoleResponse(
    @Schema(description = "ID del rol", example = "1")
    Long id,
    
    @Schema(description = "Nombre del rol", example = "USER")
    String name,
    
    @Schema(description = "Authority del rol", example = "ROLE_USER")
    String authority
) {}
