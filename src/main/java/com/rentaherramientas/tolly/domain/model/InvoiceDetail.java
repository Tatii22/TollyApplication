package com.rentaherramientas.tolly.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class InvoiceDetail {

  private final Long id;
  private final Long invoiceId;
  private final Tool tool;
  private final BigDecimal dailyPrice;
  private final int rentalDay;
  private final int quantity;
  private final BigDecimal subTotal;

  public InvoiceDetail(Long id,
                       Long invoiceId,
                       Tool tool,
                       BigDecimal dailyPrice,
                       int rentalDay,
                       int quantity,
                       BigDecimal subTotal) {
    if (tool == null) {
      throw new IllegalArgumentException("Tool is required");
    }
    if (dailyPrice == null || dailyPrice.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Daily price must be greater than 0");
    }
    if (rentalDay < 1) {
      throw new IllegalArgumentException("Rental days must be greater than 0");
    }
    if (quantity < 1) {
      throw new IllegalArgumentException("Quantity must be greater than 0");
    }
    if (subTotal == null || subTotal.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Subtotal must be zero or positive");
    }
    this.id = id;
    this.invoiceId = invoiceId;
    this.tool = tool;
    this.dailyPrice = dailyPrice;
    this.rentalDay = rentalDay;
    this.quantity = quantity;
    this.subTotal = subTotal;
  }

  public Long getId() {
    return id;
  }

  public Long getInvoiceId() {
    return invoiceId;
  }

  public Tool getTool() {
    return tool;
  }

  public BigDecimal getDailyPrice() {
    return dailyPrice;
  }

  public int getRentalDay() {
    return rentalDay;
  }

  public int getQuantity() {
    return quantity;
  }

  public BigDecimal getSubTotal() {
    return subTotal;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof InvoiceDetail)) return false;
    InvoiceDetail that = (InvoiceDetail) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
