package com.rentaherramientas.tolly.infrastructure.persistence.adapters.in.rest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rentaherramientas.tolly.application.dto.report.FrequentClientReportResponse;
import com.rentaherramientas.tolly.application.dto.report.IncomeReportResponse;
import com.rentaherramientas.tolly.application.dto.report.ToolAvailabilityReportResponse;
import com.rentaherramientas.tolly.application.dto.report.TopToolReportResponse;
import com.rentaherramientas.tolly.application.dto.reservation.ReservationResponse;
import com.rentaherramientas.tolly.application.usecase.report.GetFrequentClientsReportUseCase;
import com.rentaherramientas.tolly.application.usecase.report.GetIncomeReportUseCase;
import com.rentaherramientas.tolly.application.usecase.report.GetRentalHistoryReportUseCase;
import com.rentaherramientas.tolly.application.usecase.report.GetToolAvailabilityReportUseCase;
import com.rentaherramientas.tolly.application.usecase.report.GetTopToolsReportUseCase;

@RestController
@RequestMapping("/admin/reports")
@PreAuthorize("hasRole('ADMIN')")
public class ReportController {

  private final GetIncomeReportUseCase getIncomeReportUseCase;
  private final GetTopToolsReportUseCase getTopToolsReportUseCase;
  private final GetFrequentClientsReportUseCase getFrequentClientsReportUseCase;
  private final GetToolAvailabilityReportUseCase getToolAvailabilityReportUseCase;
  private final GetRentalHistoryReportUseCase getRentalHistoryReportUseCase;

  public ReportController(GetIncomeReportUseCase getIncomeReportUseCase,
      GetTopToolsReportUseCase getTopToolsReportUseCase,
      GetFrequentClientsReportUseCase getFrequentClientsReportUseCase,
      GetToolAvailabilityReportUseCase getToolAvailabilityReportUseCase,
      GetRentalHistoryReportUseCase getRentalHistoryReportUseCase) {
    this.getIncomeReportUseCase = getIncomeReportUseCase;
    this.getTopToolsReportUseCase = getTopToolsReportUseCase;
    this.getFrequentClientsReportUseCase = getFrequentClientsReportUseCase;
    this.getToolAvailabilityReportUseCase = getToolAvailabilityReportUseCase;
    this.getRentalHistoryReportUseCase = getRentalHistoryReportUseCase;
  }

  @GetMapping("/income")
  public ResponseEntity<IncomeReportResponse> income(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
    BigDecimal total = getIncomeReportUseCase.execute(from, to);
    return ResponseEntity.ok(new IncomeReportResponse(total));
  }

  @GetMapping("/top-tools")
  public ResponseEntity<List<TopToolReportResponse>> topTools(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
      @RequestParam(defaultValue = "5") int limit) {
    return ResponseEntity.ok(getTopToolsReportUseCase.execute(from, to, limit));
  }

  @GetMapping("/frequent-clients")
  public ResponseEntity<List<FrequentClientReportResponse>> frequentClients(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
      @RequestParam(defaultValue = "5") int limit) {
    return ResponseEntity.ok(getFrequentClientsReportUseCase.execute(from, to, limit));
  }

  @GetMapping("/availability")
  public ResponseEntity<List<ToolAvailabilityReportResponse>> availability() {
    return ResponseEntity.ok(getToolAvailabilityReportUseCase.execute());
  }

  @GetMapping("/rentals")
  public ResponseEntity<List<ReservationResponse>> rentalHistory(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
    return ResponseEntity.ok(getRentalHistoryReportUseCase.execute(from, to));
  }
}
