package com.rentaherramientas.tolly.application.dto.tool;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Herramienta de alquiler (vista publica)")
public record ToolPublicResponse(
    @Schema(description = "ID de la herramienta", example = "1")
    Long id,

    @Schema(description = "Nombre de la herramienta", example = "Taladro Dewalt")
    String name,

    @Schema(description = "Descripcion", example = "Taladro profesional 20V")
    String description,

    @Schema(description = "Costo diario", example = "25.50")
    Double dailyPrice,

    @Schema(description = "Cantidad disponible de herramientas", example = "7")
    Integer availableQuantity,

    @Schema(description = "Estado de la herramienta", example = "AVAILABLE")
    Long statusId,

    @Schema(description = "ID del proveedor", example = "847fa560-1ac3-49cb-b435-ad10b20b762d")
    UUID supplierId,

    @Schema(description = "ID de la categoria", example = "1")
    Long categoryId
) {}
