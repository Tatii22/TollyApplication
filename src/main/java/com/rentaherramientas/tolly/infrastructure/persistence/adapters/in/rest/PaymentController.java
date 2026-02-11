package com.rentaherramientas.tolly.infrastructure.persistence.adapters.in.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.rentaherramientas.tolly.application.dto.payment.PaymentResponse;
import com.rentaherramientas.tolly.application.mapper.PaymentMapper;
import com.rentaherramientas.tolly.application.usecase.payment.GetPaymentByReservationUseCase;
import com.rentaherramientas.tolly.application.usecase.payment.GetPaymentsByClientUseCase;
import com.rentaherramientas.tolly.application.usecase.payment.GetPaymentsByStatusUseCase;
import com.rentaherramientas.tolly.application.usecase.payment.GetPaymentsBySupplierUseCase;
import com.rentaherramientas.tolly.application.usecase.payment.SearchPaymentsUseCase;
import com.rentaherramientas.tolly.application.usecase.payment.PayPaymentUseCase;
import com.rentaherramientas.tolly.domain.model.Payment;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/payments")
@Tag(name = "Pagos", description = "Endpoints para gestión de pagos")
public class PaymentController {

    private final PayPaymentUseCase payPaymentUseCase;
    private final GetPaymentByReservationUseCase getPaymentByReservationUseCase;
    private final GetPaymentsByClientUseCase getPaymentsByClientUseCase;
    private final GetPaymentsByStatusUseCase getPaymentsByStatusUseCase;
    private final GetPaymentsBySupplierUseCase getPaymentsBySupplierUseCase;
    private final SearchPaymentsUseCase searchPaymentsUseCase;

    public PaymentController(PayPaymentUseCase payPaymentUseCase,
                             GetPaymentByReservationUseCase getPaymentByReservationUseCase,
                             GetPaymentsByClientUseCase getPaymentsByClientUseCase,
                             GetPaymentsByStatusUseCase getPaymentsByStatusUseCase,
                             GetPaymentsBySupplierUseCase getPaymentsBySupplierUseCase,
                             SearchPaymentsUseCase searchPaymentsUseCase) {
        this.payPaymentUseCase = payPaymentUseCase;
        this.getPaymentByReservationUseCase = getPaymentByReservationUseCase;
        this.getPaymentsByClientUseCase = getPaymentsByClientUseCase;
        this.getPaymentsByStatusUseCase = getPaymentsByStatusUseCase;
        this.getPaymentsBySupplierUseCase = getPaymentsBySupplierUseCase;
        this.searchPaymentsUseCase = searchPaymentsUseCase;
    }

    @PostMapping("/reservation/{reservationId}/pay")
    @PreAuthorize("hasRole('CLIENT')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Pagar reserva", description = "Marca el pago de una reserva del cliente autenticado")
    @ApiResponse(responseCode = "200", description = "Pago realizado exitosamente")
    public ResponseEntity<PaymentResponse> payReservation(
            @PathVariable Long reservationId,
            Authentication authentication) {

        UUID userId = (UUID) authentication.getPrincipal();
        Payment payment = payPaymentUseCase.execute(reservationId, userId);

        return ResponseEntity.ok(PaymentMapper.toResponse(payment));
    }

    @GetMapping("/reservation/{reservationId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPPLIER','CLIENT')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Obtener pago por reserva")
    @ApiResponse(responseCode = "200", description = "Pago obtenido exitosamente")
    public ResponseEntity<PaymentResponse> getByReservation(
            @PathVariable Long reservationId,
            Authentication authentication) {
        boolean validateOwner = isClient(authentication);
        UUID userId = validateOwner ? (UUID) authentication.getPrincipal() : null;
        Payment payment = getPaymentByReservationUseCase.execute(reservationId, userId, validateOwner);
        return ResponseEntity.ok(PaymentMapper.toResponse(payment));
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPPLIER','CLIENT')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar pagos por cliente")
    @ApiResponse(responseCode = "200", description = "Pagos obtenidos exitosamente")
    public ResponseEntity<List<PaymentResponse>> getByClient(
            @PathVariable Long clientId,
            Authentication authentication) {
        boolean validateOwner = isClient(authentication);
        UUID userId = validateOwner ? (UUID) authentication.getPrincipal() : null;
        List<Payment> payments = getPaymentsByClientUseCase.execute(clientId, userId, validateOwner);
        return ResponseEntity.ok(toResponses(payments));
    }

    @GetMapping("/status/{statusName}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPPLIER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar pagos por estado")
    @ApiResponse(responseCode = "200", description = "Pagos obtenidos exitosamente")
    public ResponseEntity<List<PaymentResponse>> getByStatus(
            @PathVariable String statusName) {
        List<Payment> payments = getPaymentsByStatusUseCase.execute(statusName);
        return ResponseEntity.ok(toResponses(payments));
    }

    @GetMapping("/supplier/{supplierId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPPLIER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar pagos por proveedor", description = "Filtra pagos por proveedor y rango de fechas opcional")
    @ApiResponse(responseCode = "200", description = "Pagos obtenidos exitosamente")
    public ResponseEntity<List<PaymentResponse>> getBySupplier(
            @PathVariable Long supplierId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            Authentication authentication) {
        boolean validateOwner = isSupplier(authentication);
        UUID userId = validateOwner ? (UUID) authentication.getPrincipal() : null;
        List<Payment> payments = getPaymentsBySupplierUseCase.execute(supplierId, userId, validateOwner, from, to);
        return ResponseEntity.ok(toResponses(payments));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Buscar pagos", description = "Filtra pagos por rango de fechas y estado")
    @ApiResponse(responseCode = "200", description = "Pagos obtenidos exitosamente")
    public ResponseEntity<List<PaymentResponse>> searchPayments(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) String status) {
        List<Payment> payments = searchPaymentsUseCase.execute(from, to, status);
        return ResponseEntity.ok(toResponses(payments));
    }
 ///frontend https://github.com/Killerdav02/tollyFront.git rama: examenJuan
    @GetMapping("/pagos/cliente/{id}") 
    @PreAuthorize("hasAnyRole('ADMIN','SUPPLIER','CLIENT')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar pagos por cliente")
    @ApiResponse(responseCode = "200", description = "Pagos obtenidos exitosamente")
    public ResponseEntity<List<PaymentResponse>> getByCliente(
            @PathVariable Long clientId,
            Authentication authentication) {
        boolean validateOwner = isClient(authentication);
        UUID userId = validateOwner ? (UUID) authentication.getPrincipal() : null;
        List<Payment> payments = getPaymentsByClientUseCase.execute(clientId, userId, validateOwner);
        return ResponseEntity.ok(toResponses(payments));
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

    private List<PaymentResponse> toResponses(List<Payment> payments) {
        return payments.stream()
                .map(PaymentMapper::toResponse)
                .toList();
    }
}
