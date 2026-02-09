package com.rentaherramientas.tolly.application.dto.invoice;

import java.math.BigDecimal;

public class InvoiceDetailResponse {

  private Long toolId;
  private String toolName;
  private BigDecimal dailyPrice;
  private int rentalDays;
  private int quantity;
  private BigDecimal subTotal;

  public InvoiceDetailResponse(Long toolId,
                               String toolName,
                               BigDecimal dailyPrice,
                               int rentalDays,
                               int quantity,
                               BigDecimal subTotal) {
    this.toolId = toolId;
    this.toolName = toolName;
    this.dailyPrice = dailyPrice;
    this.rentalDays = rentalDays;
    this.quantity = quantity;
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

  public int getQuantity() {
    return quantity;
  }

  public BigDecimal getSubTotal() {
    return subTotal;
  }
}
