package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "return_detail")
public class ReturnDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_return_detail")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_return", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ReturnEntity returnValue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tool", nullable = false)
    private ToolEntity tool;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "observations", columnDefinition = "TEXT")
    private String observations;

    public ReturnDetailEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ReturnEntity getReturnValue() { return returnValue; }
    public void setReturnValue(ReturnEntity returnValue) { this.returnValue = returnValue; }

    public Long getReturnId() { return returnValue != null ? returnValue.getId() : null; }
    public void setReturnId(Long returnId) {
        if (returnValue == null) returnValue = new ReturnEntity();
        returnValue.setId(returnId);
    }

    public ToolEntity getTool() { return tool; }
    public void setTool(ToolEntity tool) { this.tool = tool; }

    public Long getToolId() { return tool != null ? tool.getId() : null; }
    public void setToolId(Long toolId) {
        if (tool == null) tool = new ToolEntity();
        tool.setId(toolId);
    }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
}
