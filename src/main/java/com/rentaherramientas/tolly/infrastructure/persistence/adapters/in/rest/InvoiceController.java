package com.rentaherramientas.tolly.infrastructure.persistence.adapters.in.rest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rentaherramientas.tolly.application.dto.invoice.InvoiceResponse;
import com.rentaherramientas.tolly.application.mapper.InvoiceMapper;
import com.rentaherramientas.tolly.application.service.InvoiceRenderService;
import com.rentaherramientas.tolly.application.usecase.invoice.GetAllInvoicesUseCase;
import com.rentaherramientas.tolly.application.usecase.invoice.GetInvoiceByIdUseCase;
import com.rentaherramientas.tolly.application.usecase.invoice.GetInvoiceByPaymentUseCase;
import com.rentaherramientas.tolly.application.usecase.invoice.GetInvoicesByClientUseCase;
import com.rentaherramientas.tolly.application.usecase.invoice.GetInvoicesBySupplierUseCase;
import com.rentaherramientas.tolly.application.usecase.invoice.SearchInvoicesByClientUseCase;
import com.rentaherramientas.tolly.application.usecase.invoice.SearchInvoicesBySupplierUseCase;
import com.rentaherramientas.tolly.application.usecase.invoice.SearchInvoicesUseCase;
import com.rentaherramientas.tolly.domain.model.Invoice;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

  private final GetInvoiceByPaymentUseCase getInvoiceByPaymentUseCase;
  private final GetInvoicesByClientUseCase getInvoicesByClientUseCase;
  private final GetInvoicesBySupplierUseCase getInvoicesBySupplierUseCase;
  private final GetAllInvoicesUseCase getAllInvoicesUseCase;
  private final GetInvoiceByIdUseCase getInvoiceByIdUseCase;
  private final SearchInvoicesUseCase searchInvoicesUseCase;
  private final SearchInvoicesByClientUseCase searchInvoicesByClientUseCase;
  private final SearchInvoicesBySupplierUseCase searchInvoicesBySupplierUseCase;
  private final InvoiceRenderService invoiceRenderService;

  public InvoiceController(GetInvoiceByPaymentUseCase getInvoiceByPaymentUseCase,
                           GetInvoicesByClientUseCase getInvoicesByClientUseCase,
                           GetInvoicesBySupplierUseCase getInvoicesBySupplierUseCase,
                           GetAllInvoicesUseCase getAllInvoicesUseCase,
                           GetInvoiceByIdUseCase getInvoiceByIdUseCase,
                           SearchInvoicesUseCase searchInvoicesUseCase,
                           SearchInvoicesByClientUseCase searchInvoicesByClientUseCase,
                           SearchInvoicesBySupplierUseCase searchInvoicesBySupplierUseCase,
                           InvoiceRenderService invoiceRenderService) {
    this.getInvoiceByPaymentUseCase = getInvoiceByPaymentUseCase;
    this.getInvoicesByClientUseCase = getInvoicesByClientUseCase;
    this.getInvoicesBySupplierUseCase = getInvoicesBySupplierUseCase;
    this.getAllInvoicesUseCase = getAllInvoicesUseCase;
    this.getInvoiceByIdUseCase = getInvoiceByIdUseCase;
    this.searchInvoicesUseCase = searchInvoicesUseCase;
    this.searchInvoicesByClientUseCase = searchInvoicesByClientUseCase;
    this.searchInvoicesBySupplierUseCase = searchInvoicesBySupplierUseCase;
    this.invoiceRenderService = invoiceRenderService;
  }

  @GetMapping("/{invoiceId}")
  @PreAuthorize("hasAnyRole('ADMIN','SUPPLIER','CLIENT')")
  public ResponseEntity<InvoiceResponse> getById(
      @PathVariable Long invoiceId,
      Authentication authentication) {
    boolean validateClient = isClient(authentication);
    boolean validateSupplier = isSupplier(authentication);
    UUID userId = (validateClient || validateSupplier) ? (UUID) authentication.getPrincipal() : null;
    Invoice invoice = getInvoiceByIdUseCase.execute(invoiceId, userId, validateClient, validateSupplier);
    return ResponseEntity.ok(InvoiceMapper.toResponse(invoice));
  }

  @GetMapping("/payment/{paymentId}")
  @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
  public ResponseEntity<InvoiceResponse> getByPayment(
      @PathVariable Long paymentId,
      Authentication authentication) {
    boolean validateOwner = isClient(authentication);
    UUID userId = validateOwner ? (UUID) authentication.getPrincipal() : null;
    Invoice invoice = getInvoiceByPaymentUseCase.execute(paymentId, userId, validateOwner);
    return ResponseEntity.ok(InvoiceMapper.toResponse(invoice));
  }

  @GetMapping("/{invoiceId}/html")
  @PreAuthorize("hasAnyRole('ADMIN','SUPPLIER','CLIENT')")
  public ResponseEntity<String> downloadHtml(
      @PathVariable Long invoiceId,
      Authentication authentication) {
    boolean validateClient = isClient(authentication);
    boolean validateSupplier = isSupplier(authentication);
    UUID userId = (validateClient || validateSupplier) ? (UUID) authentication.getPrincipal() : null;
    Invoice invoice = getInvoiceByIdUseCase.execute(invoiceId, userId, validateClient, validateSupplier);
    String html = invoiceRenderService.renderHtml(invoice);
    return ResponseEntity.ok()
        .header("Content-Disposition", "attachment; filename=invoice-" + invoiceId + ".html")
        .header("Content-Type", "text/html; charset=UTF-8")
        .body(html);
  }

  @GetMapping("/{invoiceId}/pdf")
  @PreAuthorize("hasAnyRole('ADMIN','SUPPLIER','CLIENT')")
  public ResponseEntity<byte[]> downloadPdf(
      @PathVariable Long invoiceId,
      Authentication authentication) {
    boolean validateClient = isClient(authentication);
    boolean validateSupplier = isSupplier(authentication);
    UUID userId = (validateClient || validateSupplier) ? (UUID) authentication.getPrincipal() : null;
    Invoice invoice = getInvoiceByIdUseCase.execute(invoiceId, userId, validateClient, validateSupplier);
    byte[] pdf = invoiceRenderService.renderPdf(invoice);
    return ResponseEntity.ok()
        .header("Content-Disposition", "attachment; filename=invoice-" + invoiceId + ".pdf")
        .header("Content-Type", "application/pdf")
        .body(pdf);
  }

  @GetMapping("/client/{clientId}")
  @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
  public ResponseEntity<List<InvoiceResponse>> getByClient(
      @PathVariable Long clientId,
      Authentication authentication) {
    boolean validateOwner = isClient(authentication);
    UUID userId = validateOwner ? (UUID) authentication.getPrincipal() : null;
    List<Invoice> invoices = getInvoicesByClientUseCase.execute(clientId, userId, validateOwner);
    return ResponseEntity.ok(toResponses(invoices));
  }

  @GetMapping("/client/{clientId}/search")
  @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
  public ResponseEntity<List<InvoiceResponse>> searchByClient(
      @PathVariable Long clientId,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
      @RequestParam(required = false) String paymentStatus,
      Authentication authentication) {
    boolean validateOwner = isClient(authentication);
    UUID userId = validateOwner ? (UUID) authentication.getPrincipal() : null;
    List<Invoice> invoices = searchInvoicesByClientUseCase.execute(
        clientId, userId, validateOwner, from, to, paymentStatus);
    return ResponseEntity.ok(toResponses(invoices));
  }

  @GetMapping("/supplier/{supplierId}")
  @PreAuthorize("hasAnyRole('ADMIN','SUPPLIER')")
  public ResponseEntity<List<InvoiceResponse>> getBySupplier(
      @PathVariable Long supplierId,
      Authentication authentication) {
    boolean validateOwner = isSupplier(authentication);
    UUID userId = validateOwner ? (UUID) authentication.getPrincipal() : null;
    List<Invoice> invoices = getInvoicesBySupplierUseCase.execute(supplierId, userId, validateOwner);
    return ResponseEntity.ok(toResponses(invoices));
  }

  @GetMapping("/supplier/{supplierId}/search")
  @PreAuthorize("hasAnyRole('ADMIN','SUPPLIER')")
  public ResponseEntity<List<InvoiceResponse>> searchBySupplier(
      @PathVariable Long supplierId,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
      @RequestParam(required = false) String paymentStatus,
      Authentication authentication) {
    boolean validateOwner = isSupplier(authentication);
    UUID userId = validateOwner ? (UUID) authentication.getPrincipal() : null;
    List<Invoice> invoices = searchInvoicesBySupplierUseCase.execute(
        supplierId, userId, validateOwner, from, to, paymentStatus);
    return ResponseEntity.ok(toResponses(invoices));
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<InvoiceResponse>> getAll() {
    List<Invoice> invoices = getAllInvoicesUseCase.execute();
    return ResponseEntity.ok(toResponses(invoices));
  }

  @GetMapping("/search")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<InvoiceResponse>> searchAll(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
      @RequestParam(required = false) String paymentStatus) {
    List<Invoice> invoices = searchInvoicesUseCase.execute(from, to, paymentStatus);
    return ResponseEntity.ok(toResponses(invoices));
  }

  private boolean isClient(Authentication authentication) {
    if (authentication == null || authentication.getAuthorities() == null) {
      return false;
    }
    return authentication.getAuthorities().stream()
        .anyMatch(authority -> "ROLE_CLIENT".equals(authority.getAuthority()));
  }

  private boolean isSupplier(Authentication authentication) {
    if (authentication == null || authentication.getAuthorities() == null) {
      return false;
    }
    return authentication.getAuthorities().stream()
        .anyMatch(authority -> "ROLE_SUPPLIER".equals(authority.getAuthority()));
  }

  private List<InvoiceResponse> toResponses(List<Invoice> invoices) {
    return invoices.stream()
        .map(InvoiceMapper::toResponse)
        .toList();
  }
}
