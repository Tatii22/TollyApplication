package com.rentaherramientas.tolly.infrastructure.persistence.adapters.in.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.rentaherramientas.tolly.application.dto.payment.CreatePaymentRequest;
import com.rentaherramientas.tolly.application.dto.payment.PaymentResponse;
import com.rentaherramientas.tolly.application.mapper.PaymentMapper;
import com.rentaherramientas.tolly.application.usecase.payment.CreatePaymentUseCase;
import com.rentaherramientas.tolly.domain.model.Payment;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final CreatePaymentUseCase createPaymentUseCase;

    public PaymentController(CreatePaymentUseCase createPaymentUseCase) {
        this.createPaymentUseCase = createPaymentUseCase;
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody CreatePaymentRequest request) {

        Payment payment = createPaymentUseCase.execute(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PaymentMapper.toResponse(payment));
    }
}
