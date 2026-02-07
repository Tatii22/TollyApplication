package com.rentaherramientas.tolly.application.dto.report;

import java.math.BigDecimal;

public record IncomeReportResponse(
    BigDecimal totalIncome
) {}
