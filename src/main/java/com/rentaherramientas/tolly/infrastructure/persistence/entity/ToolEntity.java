package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "tools")
public class ToolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "daily_cost", nullable = false)
    private Double dailyPrice;

    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity;

    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity;

    @Column(name = "id_tool_status", nullable = false)
    private Long statusId;

    @Column(name = "id_supplier", nullable = false)
    private UUID supplierId;

    @Column(name = "id_category", nullable = false)
    private Long categoryId;

    public ToolEntity() {}

    public ToolEntity(Long id, String name, String description, Double dailyPrice,
                        Integer totalQuantity, Integer availableQuantity, Long statusId,
                        UUID supplierId, Long categoryId) {
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

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public Double getDailyPrice() {return dailyPrice;}
    public void setDailyPrice(Double dailyPrice) {this.dailyPrice = dailyPrice;}

    public Long getStatusId() {return statusId;}
    public void setStatusId(Long statusId) {this.statusId = statusId;}

    public Integer getTotalQuantity() {return totalQuantity;}
    public void setTotalQuantity(Integer totalQuantity) {this.totalQuantity = totalQuantity;}

    public Integer getAvailableQuantity() {return availableQuantity;}
    public void setAvailableQuantity(Integer availableQuantity) {this.availableQuantity = availableQuantity;}

    public UUID getSupplierId() {return supplierId;}
    public void setSupplierId(UUID supplierId) {this.supplierId = supplierId;}

    public Long getCategoryId() {return categoryId;}
    public void setCategoryId(Long categoryId) {this.categoryId = categoryId;}
}
