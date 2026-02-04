package com.rentaherramientas.tolly.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información del proveedor")
public record SupplierResponse(

    @Schema(description = "Teléfono del proveedor", example = "3142875690")
    String phone,

    @Schema(description = "Nombre de la compañía del proveedor", example = "MiCompañía")
    String company,

    @Schema(description = "Nombre del contacto", example = "Carlos")
    String contactName,

    @Schema(description = "Identificación del proveedor", example = "9876543210")
    String identification

) {}
