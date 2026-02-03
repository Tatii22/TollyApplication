package com.rentaherramientas.tolly.application.dto.tool;

public class UpdateToolRequest {
    private String name;
    private String description;
    private Double dailyCost;
    private Integer stock;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getDailyCost() { return dailyCost; }
    public void setDailyCost(Double dailyCost) { this.dailyCost = dailyCost; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}
