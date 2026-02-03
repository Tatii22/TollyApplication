package com.rentaherramientas.tolly.application.dto.tool;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Herramienta de alquiler")
public record ToolResponse(
    @Schema(description = "ID de la herramienta", example = "1")
    Long id,
    
    @Schema(description = "ID del proveedor", example = "1")
    Long supplierId,
    
    @Schema(description = "ID de la categoría", example = "1")
    Long categoryId,
    
    @Schema(description = "Nombre de la herramienta", example = "Taladro Dewalt")
    String name,
    
    @Schema(description = "Descripción", example = "Taladro profesional 20V")
    String description,
    
    @Schema(description = "Costo diario", example = "25.50")
    Double dailyCost,
    
    @Schema(description = "Estado de la herramienta", example = "AVAIBLE")
    String status,  // Como String para serializar en JSON
    
    @Schema(description = "Stock disponible", example = "10")
    Integer stock
) {}
