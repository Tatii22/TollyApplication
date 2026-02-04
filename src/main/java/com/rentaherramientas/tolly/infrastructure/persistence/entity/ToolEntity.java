package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tool_status", nullable = false)
    private ToolStatusEntity toolStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_supplier", nullable = false)
    private SupplierEntity supplier;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_category", nullable = false)
    private CategoryEntity category;

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
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public Double getDailyPrice() {return dailyPrice;}
    public void setDailyPrice(Double dailyPrice) {this.dailyPrice = dailyPrice;}

    public Long getStatusId() {return toolStatus != null ? toolStatus.getId() : null;}
    public void setStatusId(Long statusId) {
        if (toolStatus == null) toolStatus = new ToolStatusEntity();
        toolStatus.setId(statusId);
    }

    public ToolStatusEntity getToolStatus() {return toolStatus;}
    public void setToolStatus(ToolStatusEntity toolStatus) {this.toolStatus = toolStatus;}

    public Integer getTotalQuantity() {return totalQuantity;}
    public void setTotalQuantity(Integer totalQuantity) {this.totalQuantity = totalQuantity;}

    public Integer getAvailableQuantity() {return availableQuantity;}
    public void setAvailableQuantity(Integer availableQuantity) {this.availableQuantity = availableQuantity;}

    public UUID getSupplierId() {return supplier != null ? supplier.getId() : null;}
    public void setSupplierId(UUID supplierId) {
        if (supplier == null) supplier = new SupplierEntity();
        supplier.setId(supplierId);
    }

    public SupplierEntity getSupplier() {return supplier;}
    public void setSupplier(SupplierEntity supplier) {this.supplier = supplier;}

    public Long getCategoryId() {return category != null ? category.getId() : null;}
    public void setCategoryId(Long categoryId) {
        if (category == null) category = new CategoryEntity();
        category.setId(categoryId);
    }

    public CategoryEntity getCategory() {return category;}
    public void setCategory(CategoryEntity category) {this.category = category;}
}
