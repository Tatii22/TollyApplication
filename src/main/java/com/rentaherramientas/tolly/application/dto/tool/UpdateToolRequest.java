package com.rentaherramientas.tolly.application.dto.tool;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "Request para actualizar una herramienta")
public record UpdateToolRequest(
    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(description = "Nombre de la herramienta", example = "Taladro Dewalt Actualizado")
    String name,
    
    @NotBlank(message = "La descripción no puede estar vacía")
    @Schema(description = "Descripción de la herramienta", example = "Taladro profesional 18V con batería")
    String description,
    
    @NotNull(message = "El precio diario no puede ser nulo")
    @Positive(message = "El precio debe ser positivo")
    @Schema(description = "Precio diario de alquiler", example = "25.50")
    Double dailyPrice,
    
    @NotNull(message = "La cantidad total no puede ser nula")
    @Positive(message = "La cantidad total debe ser positiva")
    @Schema(description = "Cantidad total de unidades disponibles", example = "5")
    Integer totalQuantity,
    
    @NotNull(message = "La cantidad disponible no puede ser nula")
    @PositiveOrZero(message = "La cantidad disponible no puede ser negativa")
    @Schema(description = "Cantidad disponible para alquiler", example = "3")
    Integer availableQuantity,
    
    @NotNull(message = "El ID del estado no puede ser nulo")
    @Positive(message = "El ID del estado debe ser positivo")
    @Schema(description = "ID del estado de la herramienta", example = "1")
    Long statusId,
    
    @NotNull(message = "El ID del proveedor no puede ser nulo")
    @Schema(description = "ID del proveedor", example = "1")
    Long supplierId,
    
    @NotNull(message = "El ID de la categoría no puede ser nulo")
    @Positive(message = "El ID de la categoría debe ser positivo")
    @Schema(description = "ID de la categoría", example = "1")
    Long categoryId
) {}
