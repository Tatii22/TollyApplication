package com.rentaherramientas.tolly.infrastructure.persistence.adapters.in.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.rentaherramientas.tolly.application.dto.reservation.ReservationRequest;
import com.rentaherramientas.tolly.application.dto.reservation.ReservationResponse;
import com.rentaherramientas.tolly.application.usecase.reservation.ReservationCancelledUseCase;
import com.rentaherramientas.tolly.application.usecase.reservation.CreateReservationUseCase;
import com.rentaherramientas.tolly.application.usecase.reservation.MarkReservationFinishedUseCase;
import com.rentaherramientas.tolly.application.usecase.reservation.MarkReservationIncidentUseCase;
import com.rentaherramientas.tolly.application.usecase.reservation.ReservationListUseCase;
import com.rentaherramientas.tolly.application.usecase.reservation.ReservationStatusUseCase;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Reservations", description = "Gestión de reservas")
public class ReservationController {

  private final ReservationCancelledUseCase reservationCancelledUseCase;
  private final CreateReservationUseCase reservationCreate;
  private final ReservationListUseCase reservationList;
  private final ReservationStatusUseCase reservationStatus;
  private final MarkReservationFinishedUseCase markReservationFinishedUseCase;
  private final MarkReservationIncidentUseCase markReservationIncidentUseCase;

  public ReservationController(ReservationCancelledUseCase reservationCancelledUseCase,
      CreateReservationUseCase reservationCreate, ReservationListUseCase reservationList,
      ReservationStatusUseCase reservationStatus,
      MarkReservationFinishedUseCase markReservationFinishedUseCase,
      MarkReservationIncidentUseCase markReservationIncidentUseCase) {
    this.reservationCancelledUseCase = reservationCancelledUseCase;
    this.reservationCreate = reservationCreate;
    this.reservationList = reservationList;
    this.reservationStatus = reservationStatus;
    this.markReservationFinishedUseCase = markReservationFinishedUseCase;
    this.markReservationIncidentUseCase = markReservationIncidentUseCase;
  }

  // -------------------------------------------------
  // CREAR RESERVA (CLIENT)
  // -------------------------------------------------
  @PreAuthorize("hasRole('CLIENT')")
  @PostMapping
  public ResponseEntity<ReservationResponse> createReservation(
      @RequestBody ReservationRequest request,
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
  public ResponseEntity<List<ReservationResponse>> getReservationsByClient(
      @PathVariable Long clientId) {

    List<ReservationResponse> responses = reservationList.getReservationsByClient(clientId);

    return ResponseEntity.ok(responses);
  }

  // -------------------------------------------------
  // CANCELAR RESERVA (CLIENT)
  // -------------------------------------------------
  @PreAuthorize("hasRole('CLIENT')")
  @PutMapping("/{id}/cancel")
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
  public ResponseEntity<List<ReservationResponse>> getReservationsByStatus(
      @PathVariable String statusName) {

    List<ReservationResponse> responses = reservationStatus.getReservationsByStatus(statusName);

    return ResponseEntity.ok(responses);
  }
}
