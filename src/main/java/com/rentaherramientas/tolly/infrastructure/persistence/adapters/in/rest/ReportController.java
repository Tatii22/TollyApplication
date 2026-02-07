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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/reports")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Reportes", description = "Endpoints de reportes y estadísticas (solo ADMIN)")
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
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Ingresos totales", description = "Suma de pagos PAID en un rango de fechas opcional")
  @ApiResponse(responseCode = "200", description = "Reporte de ingresos generado")
  public ResponseEntity<IncomeReportResponse> income(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
    BigDecimal total = getIncomeReportUseCase.execute(from, to);
    return ResponseEntity.ok(new IncomeReportResponse(total));
  }

  @GetMapping("/top-tools")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Herramientas más alquiladas", description = "Ranking por cantidad alquilada en un rango de fechas")
  @ApiResponse(responseCode = "200", description = "Reporte de herramientas más alquiladas")
  public ResponseEntity<List<TopToolReportResponse>> topTools(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
      @RequestParam(defaultValue = "5") int limit) {
    return ResponseEntity.ok(getTopToolsReportUseCase.execute(from, to, limit));
  }

  @GetMapping("/frequent-clients")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Clientes frecuentes", description = "Ranking por número de reservas en un rango de fechas")
  @ApiResponse(responseCode = "200", description = "Reporte de clientes frecuentes")
  public ResponseEntity<List<FrequentClientReportResponse>> frequentClients(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
      @RequestParam(defaultValue = "5") int limit) {
    return ResponseEntity.ok(getFrequentClientsReportUseCase.execute(from, to, limit));
  }

  @GetMapping("/availability")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Disponibilidad de equipos", description = "Lista con cantidades y estado actual")
  @ApiResponse(responseCode = "200", description = "Reporte de disponibilidad")
  public ResponseEntity<List<ToolAvailabilityReportResponse>> availability() {
    return ResponseEntity.ok(getToolAvailabilityReportUseCase.execute());
  }

  @GetMapping("/rentals")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Historial de alquileres", description = "Lista de reservas en un rango de fechas")
  @ApiResponse(responseCode = "200", description = "Reporte de historial de alquileres")
  public ResponseEntity<List<ReservationResponse>> rentalHistory(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
    return ResponseEntity.ok(getRentalHistoryReportUseCase.execute(from, to));
  }
}
