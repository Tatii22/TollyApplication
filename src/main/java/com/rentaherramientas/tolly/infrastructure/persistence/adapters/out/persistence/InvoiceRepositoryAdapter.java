package com.rentaherramientas.tolly.infrastructure.persistence.adapters.out.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.rentaherramientas.tolly.application.mapper.InvoiceMapper;
import com.rentaherramientas.tolly.domain.model.Invoice;
import com.rentaherramientas.tolly.domain.model.InvoiceDetail;
import com.rentaherramientas.tolly.domain.ports.InvoiceRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.InvoiceDetailEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.InvoiceEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.PaymentEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ToolEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.InvoiceJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.PaymentJpaRepository;

@Repository
public class InvoiceRepositoryAdapter implements InvoiceRepository {

  private final InvoiceJpaRepository invoiceJpaRepository;
  private final PaymentJpaRepository paymentJpaRepository;

  public InvoiceRepositoryAdapter(InvoiceJpaRepository invoiceJpaRepository,
                                  PaymentJpaRepository paymentJpaRepository) {
    this.invoiceJpaRepository = invoiceJpaRepository;
    this.paymentJpaRepository = paymentJpaRepository;
  }

  @Override
  public Invoice save(Invoice invoice) {
    if (invoice.getPayment() == null || invoice.getPayment().getId() == null) {
      throw new IllegalArgumentException("Payment is required for invoice");
    }

    PaymentEntity paymentEntity = paymentJpaRepository.findById(invoice.getPayment().getId())
        .orElseThrow(() -> new IllegalArgumentException("Payment not found: " + invoice.getPayment().getId()));

    InvoiceEntity entity;
    if (invoice.getId() != null) {
      entity = invoiceJpaRepository.findById(invoice.getId())
          .orElseThrow(() -> new IllegalArgumentException("Invoice not found: " + invoice.getId()));
      entity.setCode(invoice.getCode());
      entity.setIssueDate(invoice.getIssueDate());
      entity.setTotal(invoice.getTotal());
      entity.setPayment(paymentEntity);
      entity.getDetails().clear();
    } else {
      entity = new InvoiceEntity();
      entity.setCode(invoice.getCode());
      entity.setPayment(paymentEntity);
      entity.setIssueDate(invoice.getIssueDate());
      entity.setTotal(invoice.getTotal());
    }

    for (InvoiceDetail detail : invoice.getDetails()) {
      InvoiceDetailEntity detailEntity = new InvoiceDetailEntity();
      detailEntity.setInvoice(entity);
      ToolEntity tool = new ToolEntity();
      tool.setId(detail.getTool().getId());
      detailEntity.setTool(tool);
      detailEntity.setDailyPrice(detail.getDailyPrice());
      detailEntity.setRentalDay(detail.getRentalDay());
      detailEntity.setQuantity(detail.getQuantity());
      detailEntity.setSubTotal(detail.getSubTotal());
      entity.getDetails().add(detailEntity);
    }

    InvoiceEntity saved = invoiceJpaRepository.save(entity);
    return InvoiceMapper.toDomain(saved);
  }

  @Override
  public Optional<Invoice> findById(Long id) {
    return invoiceJpaRepository.findById(id).map(InvoiceMapper::toDomain);
  }

  @Override
  public Optional<Invoice> findByPaymentId(Long paymentId) {
    return invoiceJpaRepository.findByPayment_Id(paymentId).map(InvoiceMapper::toDomain);
  }

  @Override
  public boolean existsByPaymentId(Long paymentId) {
    return invoiceJpaRepository.existsByPayment_Id(paymentId);
  }

  @Override
  public List<Invoice> findByClientId(Long clientId) {
    return invoiceJpaRepository.findByPayment_Reservation_Client_Id(clientId)
        .stream()
        .map(InvoiceMapper::toDomain)
        .toList();
  }

  @Override
  public List<Invoice> findBySupplierId(Long supplierId) {
    return invoiceJpaRepository.findBySupplierId(supplierId)
        .stream()
        .map(InvoiceMapper::toDomain)
        .toList();
  }

  @Override
  public List<Invoice> findAll() {
    return invoiceJpaRepository.findAll()
        .stream()
        .map(InvoiceMapper::toDomain)
        .toList();
  }

  @Override
  public List<Invoice> searchAll(LocalDateTime from, LocalDateTime to, String paymentStatus) {
    return invoiceJpaRepository.searchAll(from, to, paymentStatus)
        .stream()
        .map(InvoiceMapper::toDomain)
        .toList();
  }

  @Override
  public List<Invoice> searchByClientId(Long clientId, LocalDateTime from, LocalDateTime to, String paymentStatus) {
    return invoiceJpaRepository.searchByClientId(clientId, from, to, paymentStatus)
        .stream()
        .map(InvoiceMapper::toDomain)
        .toList();
  }

  @Override
  public List<Invoice> searchBySupplierId(Long supplierId, LocalDateTime from, LocalDateTime to, String paymentStatus) {
    return invoiceJpaRepository.searchBySupplierId(supplierId, from, to, paymentStatus)
        .stream()
        .map(InvoiceMapper::toDomain)
        .toList();
  }
}
