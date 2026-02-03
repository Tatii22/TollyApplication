package com.rentaherramientas.tolly.domain.model;
import com.rentaherramientas.tolly.domain.model.enums.ToolStatus;

public class Tool {
    private Long id;
    private Long supplierId;
    private Long categoryId;
    private String name;
    private String description;
    private Double dailyCost;
    private ToolStatus status;
    private Integer stock;

    public Tool() {
}

    public Tool(Long id,
                Long supplierId,
                Long categoryId,
                String name,
                String description,
                Double dailyCost,
                ToolStatus status,
                Integer stock) {

        if (supplierId == null || supplierId <= 0) {
            throw new IllegalArgumentException("El ID del proveedor no puede ser nulo o menor o igual a cero");
        }
        if (categoryId == null || categoryId <= 0) {
            throw new IllegalArgumentException("El ID de la categoría no puede ser nulo o menor o igual a cero");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre de la herramienta no puede ser nulo o vacío");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("La descripción de la herramienta no puede ser nula o vacía");
        }
        if (dailyCost == null || dailyCost < 0) {
            throw new IllegalArgumentException("El costo diario no puede ser nulo o negativo");
        }
        if (status == null) {
            throw new IllegalArgumentException("El estado de la herramienta no puede ser nulo o vacío");
        }

        this.id = id;
        this.supplierId = supplierId;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.dailyCost = dailyCost;
        this.status = status;
        this.stock = stock;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Long getSupplierId() {return supplierId;}
    public void setSupplierId(Long supplierId) {this.supplierId = supplierId;}

    public Long getCategoryId() {return categoryId;}
    public void setCategoryId(Long categoryId) {this.categoryId = categoryId;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public Double getDailyCost() {return dailyCost;}
    public void setDailyCost(Double dailyCost) {this.dailyCost = dailyCost;}

    public ToolStatus getStatus() {return status;}
    public void setStatus(ToolStatus status) {this.status = status;}

    public Integer getStock() {return stock;}
    public void setStock(Integer stock) {this.stock = stock;}
}
