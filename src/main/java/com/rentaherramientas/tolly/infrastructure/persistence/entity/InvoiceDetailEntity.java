package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "invoice_detail")
public class InvoiceDetailEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_invoice_detail")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "id_invoice", nullable = false)
  private InvoiceEntity invoice;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "id_tool", nullable = false)
  private ToolEntity tool;

  @Column(name = "daily_price", nullable = false, precision = 10, scale = 2)
  private BigDecimal dailyPrice;

  @Column(name = "rental_days", nullable = false)
  private Integer rentalDay;

  @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
  private BigDecimal subTotal;

  public InvoiceDetailEntity() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public InvoiceEntity getInvoice() {
    return invoice;
  }

  public void setInvoice(InvoiceEntity invoice) {
    this.invoice = invoice;
  }

  public ToolEntity getTool() {
    return tool;
  }

  public void setTool(ToolEntity tool) {
    this.tool = tool;
  }

  public BigDecimal getDailyPrice() {
    return dailyPrice;
  }

  public void setDailyPrice(BigDecimal dailyPrice) {
    this.dailyPrice = dailyPrice;
  }

  public Integer getRentalDay() {
    return rentalDay;
  }

  public void setRentalDay(Integer rentalDay) {
    this.rentalDay = rentalDay;
  }

  public BigDecimal getSubTotal() {
    return subTotal;
  }

  public void setSubTotal(BigDecimal subTotal) {
    this.subTotal = subTotal;
  }
}
