package com.rentaherramientas.tolly.application.dto.tool;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * DTO para crear una nueva herramienta
 */
@Schema(description = "Request para crear una herramienta")
public record CreateToolRequest(
    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(description = "Nombre de la herramienta", example = "Taladro Dewalt")
    String name,
    
    @NotBlank(message = "La descripción no puede estar vacía")
    @Schema(description = "Descripción de la herramienta", example = "Taladro profesional 20V")
    String description,
    
    @NotNull(message = "El precio diario no puede ser nulo")
    @Positive(message = "El precio debe ser positivo")
    @Schema(description = "Precio diario de alquiler", example = "25.50")
    Double dailyPrice,
    
    @NotNull(message = "La cantidad total no puede ser nula")
    @Positive(message = "La cantidad debe ser positiva")
    @Schema(description = "Cantidad total de herramientas", example = "10")
    Integer totalQuantity,
    
    @NotNull(message = "La cantidad disponible no puede ser nula")
    @PositiveOrZero(message = "La cantidad debe ser >= 0")
    @Schema(description = "Cantidad disponible para alquiler", example = "7")
    Integer availableQuantity,
    
    @NotNull(message = "El estado no puede ser nulo")
    @Schema(description = "ID del estado de la herramienta", example = "1")
    Long statusId,
    
    @NotNull(message = "El proveedor no puede ser nulo")
    @Schema(description = "ID del proveedor", example = "1")
    Long supplierId,
    
    @NotNull(message = "La categoría no puede ser nula")
    @Schema(description = "ID de la categoría", example = "1")
    Long categoryId
) {}
