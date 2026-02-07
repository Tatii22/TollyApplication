package com.rentaherramientas.tolly.application.dto.report;

public record FrequentClientReportResponse(
    Long clientId,
    String clientName,
    Long reservationCount
) {}
