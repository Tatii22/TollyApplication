package com.rentaherramientas.tolly.application.usecase.payment;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.domain.model.Payment;
import com.rentaherramientas.tolly.domain.ports.PaymentRepository;

@Service
public class SearchPaymentsUseCase {

  private final PaymentRepository paymentRepository;

  public SearchPaymentsUseCase(PaymentRepository paymentRepository) {
    this.paymentRepository = paymentRepository;
  }

  public List<Payment> execute(LocalDateTime from, LocalDateTime to, String statusName) {
    String status = (statusName == null || statusName.isBlank()) ? null : statusName;
    return paymentRepository.findByDateRange(from, to, status);
  }
}
