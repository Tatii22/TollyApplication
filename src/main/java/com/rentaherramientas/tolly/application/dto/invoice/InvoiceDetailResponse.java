package com.rentaherramientas.tolly.application.dto.invoice;

import java.math.BigDecimal;

public class InvoiceDetailResponse {

  private Long toolId;
  private String toolName;
  private BigDecimal dailyPrice;
  private int rentalDays;
  private BigDecimal subTotal;

  public InvoiceDetailResponse(Long toolId,
                               String toolName,
                               BigDecimal dailyPrice,
                               int rentalDays,
                               BigDecimal subTotal) {
    this.toolId = toolId;
    this.toolName = toolName;
    this.dailyPrice = dailyPrice;
    this.rentalDays = rentalDays;
    this.subTotal = subTotal;
  }

  public Long getToolId() {
    return toolId;
  }

  public String getToolName() {
    return toolName;
  }

  public BigDecimal getDailyPrice() {
    return dailyPrice;
  }

  public int getRentalDays() {
    return rentalDays;
  }

  public BigDecimal getSubTotal() {
    return subTotal;
  }
}
