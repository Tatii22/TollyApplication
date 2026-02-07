package com.rentaherramientas.tolly.application.dto.report;

public record TopToolReportResponse(
    Long toolId,
    String toolName,
    Long totalQuantity
) {}
