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
import com.rentaherramientas.tolly.application.usecase.payment.PayPaymentUseCase;
import com.rentaherramientas.tolly.domain.model.Payment;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PayPaymentUseCase payPaymentUseCase;
    private final GetPaymentByReservationUseCase getPaymentByReservationUseCase;
    private final GetPaymentsByClientUseCase getPaymentsByClientUseCase;
    private final GetPaymentsByStatusUseCase getPaymentsByStatusUseCase;

    public PaymentController(PayPaymentUseCase payPaymentUseCase,
                             GetPaymentByReservationUseCase getPaymentByReservationUseCase,
                             GetPaymentsByClientUseCase getPaymentsByClientUseCase,
                             GetPaymentsByStatusUseCase getPaymentsByStatusUseCase) {
        this.payPaymentUseCase = payPaymentUseCase;
        this.getPaymentByReservationUseCase = getPaymentByReservationUseCase;
        this.getPaymentsByClientUseCase = getPaymentsByClientUseCase;
        this.getPaymentsByStatusUseCase = getPaymentsByStatusUseCase;
    }

    @PostMapping("/reservation/{reservationId}/pay")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<PaymentResponse> payReservation(
            @PathVariable Long reservationId,
            Authentication authentication) {

        UUID userId = (UUID) authentication.getPrincipal();
        Payment payment = payPaymentUseCase.execute(reservationId, userId);

        return ResponseEntity.ok(PaymentMapper.toResponse(payment));
    }

    @GetMapping("/reservation/{reservationId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPPLIER','CLIENT')")
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
    public ResponseEntity<List<PaymentResponse>> getByStatus(
            @PathVariable String statusName) {
        List<Payment> payments = getPaymentsByStatusUseCase.execute(statusName);
        return ResponseEntity.ok(toResponses(payments));
    }

    private boolean isClient(Authentication authentication) {
        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_CLIENT".equals(authority.getAuthority()));
    }

    private List<PaymentResponse> toResponses(List<Payment> payments) {
        return payments.stream()
                .map(PaymentMapper::toResponse)
                .toList();
    }
}
