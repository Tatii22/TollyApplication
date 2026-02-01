package com.rentaherramientas.tolly.application.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para registro de usuario
 */
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

    @Schema(description = "Rol del usuario permitido en registro",example = "CLIENT")
    @NotBlank(message = "El rol es obligatorio")
    String role,

    @Schema(description = "direccion para ingresar ")
    @NotBlank(message = "La direccion es importante")
    String address,

    @Schema(description = "Telefono debe ser de 10 digitos ", example = "314287569")
    @NotBlank(message = "el telefono debe ser obligatorio")
    String phone,

    @Schema(description = "Ingrese el nombre de la compañia")
    @NotBlank(message = "la compañia debe ser obligatoria")
    String company
) {}
