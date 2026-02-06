package com.rentaherramientas.tolly.domain.model;

public class Tool {
    private Long id;
    private String name;
    private String description;
    private Double dailyPrice;
    private Integer totalQuantity;
    private Integer availableQuantity;
    private Long statusId;
    private Long supplierId;
    private Long categoryId;

    public Tool() {
}

    public Tool(Long id,
                String name,
                String description,
                Double dailyPrice,
                Integer totalQuantity,
                Integer availableQuantity,
                Long statusId,
                Long supplierId,
                Long categoryId) {

        if (supplierId == null) {
            throw new IllegalArgumentException("El ID del proveedor no puede ser nulo");
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
        if (dailyPrice == null || dailyPrice < 0) {
            throw new IllegalArgumentException("El costo diario no puede ser nulo o negativo");
        }

        this.id = id;
        this.name = name;
        this.description = description;
        this.dailyPrice = dailyPrice;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = availableQuantity;
        this.statusId = statusId;
        this.supplierId = supplierId;
        this.categoryId = categoryId;
    }

    public static Tool reconstruct(
        Long id,
        String name,
        String description,
        Double dailyPrice,
        Integer totalQuantity,
        Integer availableQuantity,
        Long statusId,
        Long supplierId,
        Long categoryId
) {
    Tool tool = new Tool();
    tool.id = id;
    tool.name = name;
    tool.description = description;
    tool.dailyPrice = dailyPrice;
    tool.totalQuantity = totalQuantity;
    tool.availableQuantity = availableQuantity;
    tool.statusId = statusId;
    tool.supplierId = supplierId;
    tool.categoryId = categoryId;
    return tool;
}

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public Double getDailyPrice() {return dailyPrice;}
    public void setDailyPrice(Double dailyPrice) {this.dailyPrice = dailyPrice;}

    public Long getSupplierId() {return supplierId;}
    public void setSupplierId(Long supplierId) {this.supplierId = supplierId;}

    public Long getCategoryId() {return categoryId;}
    public void setCategoryId(Long categoryId) {this.categoryId = categoryId;}

    public Integer getTotalQuantity() {return totalQuantity;}
    public void setTotalQuantity(Integer totalQuantity) {this.totalQuantity = totalQuantity;}

    public Integer getAvailableQuantity() {return availableQuantity;}
    public void setAvailableQuantity(Integer availableQuantity) {this.availableQuantity = availableQuantity;}

    public Long getStatusId() {return statusId;}
    public void setStatusId(Long statusId) {this.statusId = statusId;
    }
}
