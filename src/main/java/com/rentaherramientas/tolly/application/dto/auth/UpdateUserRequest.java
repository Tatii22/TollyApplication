package com.rentaherramientas.tolly.application.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(description = "Request para actualizar usuario (cliente o proveedor)")
public record UpdateUserRequest(

    @Schema(description = "Email del usuario", example = "user@example.com")
    @Email(message = "Email debe tener un formato válido")
    String email,

    @Schema(description = "Contraseña del usuario (mínimo 6 caracteres)", example = "password123")
    @Size(min = 6, message = "Password debe tener al menos 6 caracteres")
    String password,

    @Schema(description = "Nuevo rol del usuario", example = "CLIENT")
    String role,

    // CLIENT
    @Schema(description = "Nombre del cliente (solo CLIENT)")
    String firstName,

    @Schema(description = "Apellido del cliente (solo CLIENT)")
    String lastName,

    @Schema(description = "Dirección del cliente (solo CLIENT)")
    String address,

    @Schema(description = "Número de documento (solo CLIENT)")
    String document,

    @Schema(description = "Teléfono del cliente o proveedor")
    String phone,

    // SUPPLIER
    @Schema(description = "Nombre de la compañía del proveedor (solo SUPPLIER)")
    String company,

    @Schema(description = "Identificación del proveedor (solo SUPPLIER)")
    String identification,

    @Schema(description = "Nombre del contacto del proveedor (solo SUPPLIER)")
    String contactName,

    @Schema(description = "Nuevo estado del usuario", example = "ACTIVE")
    @NotNull
    UserStatusResponse status

) {}
