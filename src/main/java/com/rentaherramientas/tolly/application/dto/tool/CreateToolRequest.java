package com.rentaherramientas.tolly.application.dto.tool;

public class CreateToolRequest {
    private Long supplierId;
    private Long categoryId;
    private String name;
    private String description;
    private Double dailyCost;
    private String status;

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
    
    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
}