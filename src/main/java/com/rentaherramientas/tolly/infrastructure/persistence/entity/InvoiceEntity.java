package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
public class InvoiceEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_invoice")
  private Long id;

  @Column(name = "code", nullable = false, unique = true, length = 30)
  private String code;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "id_payment", nullable = false)
  private PaymentEntity payment;

  @Column(name = "issue_date", nullable = false)
  private LocalDateTime issueDate;

  @Column(name = "total", nullable = false, precision = 10, scale = 2)
  private BigDecimal total;

  @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<InvoiceDetailEntity> details = new ArrayList<>();

  public InvoiceEntity() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public PaymentEntity getPayment() {
    return payment;
  }

  public void setPayment(PaymentEntity payment) {
    this.payment = payment;
  }

  public LocalDateTime getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(LocalDateTime issueDate) {
    this.issueDate = issueDate;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public List<InvoiceDetailEntity> getDetails() {
    return details;
  }

  public void setDetails(List<InvoiceDetailEntity> details) {
    this.details = details != null ? details : new ArrayList<>();
  }

  @PrePersist
  public void prePersist() {
    if (issueDate == null) {
      issueDate = LocalDateTime.now();
    }
  }
}
