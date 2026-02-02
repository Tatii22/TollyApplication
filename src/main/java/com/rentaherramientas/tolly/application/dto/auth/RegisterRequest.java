package com.rentaherramientas.tolly.application.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Solicitud de registro de nuevo usuario")
public record RegisterRequest(
    @Schema(description = "Email del usuario", example = "user@example.com", required = true)
    @NotBlank(message = "Email es requerido")
    @Email(message = "Email debe tener un formato válido")
    String email,

    @Schema(description = "Contraseña del usuario (mínimo 6 caracteres)", example = "password123", required = true)
    @NotBlank(message = "Password es requerido")
    @Size(min = 6, message = "Password debe tener al menos 6 caracteres")
    String password,

    @Schema(description = "Rol del usuario permitido en registro", example = "CLIENT")
    @NotBlank(message = "El rol es obligatorio")
    String role,

    @Schema(description = "Dirección del cliente (solo CLIENT)")
    String address,

    @Schema(description = "Teléfono del proveedor (solo SUPPLIER)")
    String phone,

    @Schema(description = "Nombre de la compañía del proveedor (solo SUPPLIER)")
    String company
) {}
