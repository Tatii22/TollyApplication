package com.rentaherramientas.tolly.application.dto.tool;

/**
 * DTO para crear una nueva herramienta
 * El estado inicial se asigna automáticamente como AVAIBLE (disponible)
 */
public class CreateToolRequest {
    private Long supplierId;
    private Long categoryId;
    private String name;
    private String description;
    private Double dailyCost;
    private Integer stock;

    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getDailyCost() { return dailyCost; }
    public void setDailyCost(Double dailyCost) { this.dailyCost = dailyCost; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}