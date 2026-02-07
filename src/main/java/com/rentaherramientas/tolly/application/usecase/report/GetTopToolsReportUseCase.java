package com.rentaherramientas.tolly.application.usecase.report;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.application.dto.report.TopToolReportResponse;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ReservationDetailJpaRepository;

@Service
public class GetTopToolsReportUseCase {

  private final ReservationDetailJpaRepository reservationDetailJpaRepository;

  public GetTopToolsReportUseCase(ReservationDetailJpaRepository reservationDetailJpaRepository) {
    this.reservationDetailJpaRepository = reservationDetailJpaRepository;
  }

  public List<TopToolReportResponse> execute(LocalDate from, LocalDate to, int limit) {
    List<Object[]> rows = reservationDetailJpaRepository.findTopTools(from, to);
    return rows.stream()
        .limit(limit)
        .map(row -> new TopToolReportResponse(
            (Long) row[0],
            (String) row[1],
            ((Number) row[2]).longValue()))
        .toList();
  }
}
