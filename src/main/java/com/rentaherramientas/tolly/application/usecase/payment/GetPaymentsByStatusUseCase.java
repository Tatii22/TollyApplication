package com.rentaherramientas.tolly.application.usecase.payment;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Payment;
import com.rentaherramientas.tolly.domain.ports.PaymentRepository;

@Service
public class GetPaymentsByStatusUseCase {

    private final PaymentRepository paymentRepository;

    public GetPaymentsByStatusUseCase(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> execute(String statusName) {
        if (statusName == null || statusName.isBlank()) {
            throw new DomainException("Status name is required");
        }
        return paymentRepository.findByStatusName(statusName);
    }
}
