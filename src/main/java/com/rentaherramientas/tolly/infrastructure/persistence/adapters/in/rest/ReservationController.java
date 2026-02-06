package com.rentaherramientas.tolly.infrastructure.persistence.adapters.in.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.rentaherramientas.tolly.application.dto.reservation.ReservationRequest;
import com.rentaherramientas.tolly.application.dto.reservation.ReservationResponse;
import com.rentaherramientas.tolly.application.usecase.Reservation.ReservationCancelledUseCase;
import com.rentaherramientas.tolly.application.usecase.Reservation.CreateReservationUseCase;
import com.rentaherramientas.tolly.application.usecase.Reservation.ReservationListUseCase;
import com.rentaherramientas.tolly.application.usecase.Reservation.ReservationStatusUseCase;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Reservations", description = "Gestión de reservas")
public class ReservationController {

  private final ReservationCancelledUseCase reservationCancelledUseCase;
  private final CreateReservationUseCase reservationCreate;
  private final ReservationListUseCase reservationList;
  private final ReservationStatusUseCase reservationStatus;

  public ReservationController(ReservationCancelledUseCase reservationCancelledUseCase,
      CreateReservationUseCase reservationCreate, ReservationListUseCase reservationList,
      ReservationStatusUseCase reservationStatus) {
    this.reservationCancelledUseCase = reservationCancelledUseCase;
    this.reservationCreate = reservationCreate;
    this.reservationList = reservationList;
    this.reservationStatus = reservationStatus;
  }

  // -------------------------------------------------
  // CREAR RESERVA (CLIENT)
  // -------------------------------------------------
  @PreAuthorize("hasRole('CLIENT')")
  @PostMapping
  public ResponseEntity<ReservationResponse> createReservation(
      @RequestBody ReservationRequest request) {

    ReservationResponse response = reservationCreate.createReservation(request);
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
  public ResponseEntity<ReservationResponse> cancelReservation(@PathVariable Long id) {
    ReservationResponse response = reservationCancelledUseCase.cancelReservation(id);
    return ResponseEntity.ok(response);
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
