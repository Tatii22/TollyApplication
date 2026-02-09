package com.rentaherramientas.tolly.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información del cliente")
public record ClientResponse(

    @Schema(description = "ID del cliente", example = "1")
    Long id,

    @Schema(description = "Dirección del cliente", example = "Calle 123 #45-67")
    String address,

    @Schema(description = "Teléfono del cliente", example = "3101234567")
    String phone,

    @Schema(description = "Nombre del cliente", example = "Juan")
    String firstName,

    @Schema(description = "Apellido del cliente", example = "Pérez")
    String lastName,

    @Schema(description = "Número de documento", example = "1234567890")
    String document

    ) {
        public static ClientResponse fromDomain(com.rentaherramientas.tolly.domain.model.Client client) {
            return new ClientResponse(
                client.getId(),
                client.getAddress(),
                client.getPhone(),
                client.getFirstName(),
                client.getLastName(),
                client.getDocument()
            );
        }
    }
