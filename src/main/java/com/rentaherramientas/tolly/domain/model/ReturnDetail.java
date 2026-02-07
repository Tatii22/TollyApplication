package com.rentaherramientas.tolly.domain.model;

public class ReturnDetail {

    private Long id;
    private Return returnValue;
    private Tool tool;
    private Integer quantity;
    private String observations;

    private ReturnDetail() {
    }

    private ReturnDetail(
        Long id,
        Return returnValue,
        Tool tool,
        Integer quantity,
        String observations) {
        this.id = id;
        this.returnValue = returnValue;
        this.tool = tool;
        this.quantity = quantity;
        this.observations = observations;
    }

    public static ReturnDetail create(
        Return returnValue,
        Tool tool,
        Integer quantity,
        String observations) {
        validateReturn(returnValue);
        validateTool(tool);
        validateQuantity(quantity);

        return new ReturnDetail(
            null,
            returnValue,
            tool,
            quantity,
            observations
        );
    }

    public static ReturnDetail reconstruct(
        Long id,
        Return returnValue,
        Tool tool,
        Integer quantity,
        String observations) {
        validateReturn(returnValue);
        validateTool(tool);
        validateQuantity(quantity);

        return new ReturnDetail(
            id,
            returnValue,
            tool,
            quantity,
            observations
        );
    }

    private static void validateReturn(Return returnValue) {
        if (returnValue == null) {
            throw new IllegalArgumentException("La devolucion es obligatoria");
        }
    }

    private static void validateTool(Tool tool) {
        if (tool == null) {
            throw new IllegalArgumentException("La herramienta es obligatoria");
        }
    }

    private static void validateQuantity(Integer quantity) {
        if (quantity == null || quantity < 1) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
    }

    public Long getId() { return id; }
    public Return getReturnValue() { return returnValue; }
    public Tool getTool() { return tool; }
    public Integer getQuantity() { return quantity; }
    public String getObservations() { return observations; }
}
