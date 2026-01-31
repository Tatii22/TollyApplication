package com.rentaherramientas.tolly.infrastructure.persistence.entity;
import com.rentaherramientas.tolly.domain.model.enums.ToolStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tools")
public class ToolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_supplier", nullable = false)
    private Long supplierId;

    @Column(name = "id_category", nullable = false)
    private Long categoryId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "daily_cost", nullable = false)
    private Double dailyCost;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ToolStatus status;

    public ToolEntity() {}

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
}
