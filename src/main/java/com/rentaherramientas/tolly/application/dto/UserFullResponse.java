package com.rentaherramientas.tolly.application.dto;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información completa del usuario registrado")
public record UserFullResponse(

    @Schema(description = "ID del usuario", example = "550e8400-e29b-41d4-a716-446655440000")
    String id,

    @Schema(description = "Email del usuario", example = "user@example.com")
    String email,

    @Schema(description = "Roles asignados al usuario")
    Set<RoleResponse> roles,

    @Schema(description = "Estado del usuario")
    UserStatusResponse status,

    @Schema(description = "Perfil de cliente (solo si es CLIENT)")
    ClientResponse client,

    @Schema(description = "Perfil de proveedor (solo si es SUPPLIER)")
    SupplierResponse supplier

) {}
