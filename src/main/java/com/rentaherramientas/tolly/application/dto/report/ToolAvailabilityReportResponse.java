package com.rentaherramientas.tolly.application.dto.report;

public record ToolAvailabilityReportResponse(
    Long toolId,
    String toolName,
    Integer totalQuantity,
    Integer availableQuantity,
    String statusName
) {}
