package com.rentaherramientas.tolly.application.usecase.report;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.domain.ports.PaymentRepository;

@Service
public class GetIncomeReportUseCase {

  private final PaymentRepository paymentRepository;

  public GetIncomeReportUseCase(PaymentRepository paymentRepository) {
    this.paymentRepository = paymentRepository;
  }

  public BigDecimal execute(LocalDateTime from, LocalDateTime to) {
    return paymentRepository.sumPaidAmountBetweenDates(from, to);
  }
}
