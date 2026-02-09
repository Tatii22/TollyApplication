package com.rentaherramientas.tolly.infrastructure.persistence.adapters.in.rest;

import java.util.List;
import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import com.rentaherramientas.tolly.application.dto.reservation.ReservationRequest;
import com.rentaherramientas.tolly.application.dto.reservation.ReservationResponse;
import com.rentaherramientas.tolly.application.usecase.reservation.ReservationCancelledUseCase;
import com.rentaherramientas.tolly.application.usecase.reservation.CreateReservationUseCase;
import com.rentaherramientas.tolly.application.usecase.reservation.GetReservationsBySupplierUseCase;
import com.rentaherramientas.tolly.application.usecase.reservation.MarkReservationFinishedUseCase;
import com.rentaherramientas.tolly.application.usecase.reservation.MarkReservationIncidentUseCase;
import com.rentaherramientas.tolly.application.usecase.reservation.ReservationListUseCase;
import com.rentaherramientas.tolly.application.usecase.reservation.ReservationStatusUseCase;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Reservations", description = "Gestión de reservas")
public class ReservationController {

  private final ReservationCancelledUseCase reservationCancelledUseCase;
  private final CreateReservationUseCase reservationCreate;
  private final ReservationListUseCase reservationList;
  private final GetReservationsBySupplierUseCase reservationBySupplier;
  private final ReservationStatusUseCase reservationStatus;
  private final MarkReservationFinishedUseCase markReservationFinishedUseCase;
  private final MarkReservationIncidentUseCase markReservationIncidentUseCase;

  public ReservationController(ReservationCancelledUseCase reservationCancelledUseCase,
      CreateReservationUseCase reservationCreate, ReservationListUseCase reservationList,
      GetReservationsBySupplierUseCase reservationBySupplier,
      ReservationStatusUseCase reservationStatus,
      MarkReservationFinishedUseCase markReservationFinishedUseCase,
      MarkReservationIncidentUseCase markReservationIncidentUseCase) {
    this.reservationCancelledUseCase = reservationCancelledUseCase;
    this.reservationCreate = reservationCreate;
    this.reservationList = reservationList;
    this.reservationBySupplier = reservationBySupplier;
    this.reservationStatus = reservationStatus;
    this.markReservationFinishedUseCase = markReservationFinishedUseCase;
    this.markReservationIncidentUseCase = markReservationIncidentUseCase;
  }

    // -------------------------------------------------
    // OBTENER RESERVA POR ID (ADMIN, SUPPLIER, CLIENT)
    // -------------------------------------------------
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Obtener reserva por ID", description = "Retorna una reserva por su ID")
    @ApiResponse(responseCode = "200", description = "Reserva obtenida exitosamente")
    @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    public ResponseEntity<ReservationResponse> getReservationById(@PathVariable Long id) {
      ReservationResponse response = reservationList.getReservationById(id);
      if (response == null) {
        return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(response);
    }
  // -------------------------------------------------
  // CREAR RESERVA (CLIENT)
  // -------------------------------------------------
  @PreAuthorize("hasRole('CLIENT')")
  @PostMapping
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Crear reserva", description = "Crea una reserva para el cliente autenticado")
  @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente")
  public ResponseEntity<ReservationResponse> createReservation(
      @Valid @RequestBody ReservationRequest request,
      Authentication authentication) {

    ReservationResponse response = reservationCreate.createReservation(
        request,
        (java.util.UUID) authentication.getPrincipal());
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // -------------------------------------------------
  // LISTAR RESERVAS DEL CLIENTE
  // -------------------------------------------------
  @PreAuthorize("hasRole('CLIENT')")
  @GetMapping("/client/{clientId}")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Listar reservas por cliente", description = "Retorna las reservas de un cliente")
  @ApiResponse(responseCode = "200", description = "Reservas obtenidas exitosamente")
  public ResponseEntity<Page<ReservationResponse>> getReservationsByClient(
      @PathVariable Long clientId,
      @RequestParam(required = false) String statusName,
      @RequestParam(required = false) LocalDate from,
      @RequestParam(required = false) LocalDate to,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id,desc") String sort) {

    Sort sortOrder = parseSort(sort);
    PageRequest pageable = PageRequest.of(page, size, sortOrder);

    Page<ReservationResponse> responses = reservationList.getReservationsByClient(clientId, statusName, from, to,
        pageable);

    return ResponseEntity.ok(responses);
  }

  // -------------------------------------------------
  // LISTAR RESERVAS POR PROVEEDOR (SUPPLIER o ADMIN)
  // -------------------------------------------------
  @PreAuthorize("hasAnyRole('SUPPLIER','ADMIN')")
  @GetMapping("/supplier/{supplierId}")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Listar reservas por proveedor", description = "Retorna reservas por proveedor con filtros opcionales")
  @ApiResponse(responseCode = "200", description = "Reservas obtenidas exitosamente")
  public ResponseEntity<Page<ReservationResponse>> getReservationsBySupplier(
      @PathVariable Long supplierId,
      @RequestParam(required = false) String statusName,
      @RequestParam(required = false) LocalDate from,
      @RequestParam(required = false) LocalDate to,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id,desc") String sort,
      Authentication authentication) {

    boolean isAdmin = hasRole(authentication, "ROLE_ADMIN");
    java.util.UUID userId = isAdmin ? null : (java.util.UUID) authentication.getPrincipal();

    Sort sortOrder = parseSort(sort);
    PageRequest pageable = PageRequest.of(page, size, sortOrder);

    Page<ReservationResponse> responses = reservationBySupplier.execute(
        supplierId,
        statusName,
        from,
        to,
        pageable,
        userId,
        isAdmin);

    return ResponseEntity.ok(responses);
  }

  private Sort parseSort(String sort) {
    if (sort == null || sort.isBlank()) {
      return Sort.by(Sort.Direction.DESC, "id");
    }
    String[] parts = sort.split(",", 2);
    String field = parts[0].trim();
    String direction = parts.length > 1 ? parts[1].trim().toLowerCase() : "desc";
    Sort.Direction dir = "asc".equals(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
    return Sort.by(dir, field);
  }

  // -------------------------------------------------
  // CANCELAR RESERVA (CLIENT)
  // -------------------------------------------------
  @PreAuthorize("hasRole('CLIENT')")
  @PutMapping("/{id}/cancel")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Cancelar reserva", description = "Cancela una reserva del cliente autenticado")
  @ApiResponse(responseCode = "200", description = "Reserva cancelada exitosamente")
  public ResponseEntity<ReservationResponse> cancelReservation(
      @PathVariable Long id,
      Authentication authentication) {
    ReservationResponse response = reservationCancelledUseCase
        .cancelReservation(id, (java.util.UUID) authentication.getPrincipal());
    return ResponseEntity.ok(response);
  }

  // -------------------------------------------------
  // MARCAR DEVOLUCION (SUPPLIER o ADMIN)
  // -------------------------------------------------
  @PreAuthorize("hasAnyRole('SUPPLIER','ADMIN')")
  @PutMapping("/{id}/finish")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Finalizar reserva", description = "Marca una reserva como finalizada")
  @ApiResponse(responseCode = "200", description = "Reserva finalizada exitosamente")
  public ResponseEntity<ReservationResponse> finishReservation(
      @PathVariable Long id,
      Authentication authentication) {
    boolean isAdmin = hasRole(authentication, "ROLE_ADMIN");
    java.util.UUID userId = isAdmin ? null : (java.util.UUID) authentication.getPrincipal();
    ReservationResponse response = markReservationFinishedUseCase.execute(id, userId, isAdmin);
    return ResponseEntity.ok(response);
  }

  // -------------------------------------------------
  // MARCAR RESERVA EN INCIDENCIA (SUPPLIER o ADMIN)
  // -------------------------------------------------
  @PreAuthorize("hasAnyRole('SUPPLIER','ADMIN')")
  @PutMapping("/{id}/incident")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Marcar incidencia", description = "Marca una reserva en incidencia")
  @ApiResponse(responseCode = "200", description = "Reserva marcada en incidencia")
  public ResponseEntity<ReservationResponse> incidentReservation(
      @PathVariable Long id,
      Authentication authentication) {
    boolean isAdmin = hasRole(authentication, "ROLE_ADMIN");
    java.util.UUID userId = isAdmin ? null : (java.util.UUID) authentication.getPrincipal();
    ReservationResponse response = markReservationIncidentUseCase.execute(id, userId, isAdmin);
    return ResponseEntity.ok(response);
  }

  private boolean hasRole(Authentication authentication, String role) {
    if (authentication == null || authentication.getAuthorities() == null) {
      return false;
    }
    return authentication.getAuthorities().stream()
        .anyMatch(authority -> role.equals(authority.getAuthority()));
  }

  // -------------------------------------------------
  // LISTAR RESERVAS POR ESTADO (ADMIN o CLIENT)
  // -------------------------------------------------
  @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
  @GetMapping("/status/{statusName}")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Listar reservas por estado", description = "Retorna reservas filtradas por estado")
  @ApiResponse(responseCode = "200", description = "Reservas obtenidas exitosamente")
  public ResponseEntity<List<ReservationResponse>> getReservationsByStatus(
      @PathVariable String statusName) {

    List<ReservationResponse> responses = reservationStatus.getReservationsByStatus(statusName);

    return ResponseEntity.ok(responses);
  }
}
