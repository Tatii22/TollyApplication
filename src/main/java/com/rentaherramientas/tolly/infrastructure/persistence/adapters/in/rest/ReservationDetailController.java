package com.rentaherramientas.tolly.infrastructure.persistence.adapters.in.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.rentaherramientas.tolly.application.usecase.reservationdetail.CreateReservationDetailUseCase;
import com.rentaherramientas.tolly.application.usecase.reservationdetail.UpdateReservationDetailUseCase;
import com.rentaherramientas.tolly.application.usecase.reservationdetail.DeleteReservationDetailUseCase;
import com.rentaherramientas.tolly.application.usecase.reservationdetail.GetReservationDetailsByReservationUseCase;
import com.rentaherramientas.tolly.domain.model.ReservationDetail;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/reservations/details")
@Tag(name = "Reservation Details", description = "Gestion de detalles de reserva")
public class ReservationDetailController {

  private final CreateReservationDetailUseCase createUseCase;
  private final UpdateReservationDetailUseCase updateUseCase;
  private final com.rentaherramientas.tolly.application.usecase.reservationdetail.UpdateReservationDetailQuantityUseCase updateQuantityUseCase;
  private final DeleteReservationDetailUseCase deleteUseCase;
  private final GetReservationDetailsByReservationUseCase getByReservationUseCase;

  public ReservationDetailController(
      CreateReservationDetailUseCase createUseCase,
      UpdateReservationDetailUseCase updateUseCase,
      com.rentaherramientas.tolly.application.usecase.reservationdetail.UpdateReservationDetailQuantityUseCase updateQuantityUseCase,
      DeleteReservationDetailUseCase deleteUseCase,
      GetReservationDetailsByReservationUseCase getByReservationUseCase) {

    this.createUseCase = createUseCase;
    this.updateUseCase = updateUseCase;
    this.updateQuantityUseCase = updateQuantityUseCase;
    this.deleteUseCase = deleteUseCase;
    this.getByReservationUseCase = getByReservationUseCase;
  }

  /*
   * =========================
   * CREAR DETALLE (AGREGAR HERRAMIENTA)
   * =========================
   */
  @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
  @PostMapping
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Crear detalle de reserva", description = "Agrega una herramienta a la reserva")
  @ApiResponse(responseCode = "201", description = "Detalle creado exitosamente")
  public ResponseEntity<ReservationDetail> create(
      @Parameter(description = "ID de la reserva", example = "1")
      @RequestParam Long reservationId,
      @Parameter(description = "ID de la herramienta", example = "1")
      @RequestParam Long toolId,
      @Parameter(description = "Cantidad a reservar", example = "1")
      @RequestParam(defaultValue = "1") int quantity) {

    ReservationDetail detail = createUseCase.execute(reservationId, toolId, quantity);

    return ResponseEntity.status(HttpStatus.CREATED).body(detail);
  }

  /*
   * =========================
   * LISTAR DETALLES DE UNA RESERVA
   * =========================
   */
  @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
  @GetMapping("/reservation/{reservationId}")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Listar detalles por reserva", description = "Retorna detalles de una reserva")
  @ApiResponse(responseCode = "200", description = "Detalles obtenidos exitosamente")
  public ResponseEntity<List<ReservationDetail>> getByReservation(
      @Parameter(description = "ID de la reserva", example = "1")
      @PathVariable Long reservationId) {

    return ResponseEntity.ok(
        getByReservationUseCase.execute(reservationId));
  }

  /*
   * =========================
   * ACTUALIZAR DETALLE (días / precio)
   * =========================
   */
  @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
  @PutMapping("/{detailId}")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Actualizar detalle de reserva", description = "Actualiza dias de alquiler")
  @ApiResponse(responseCode = "200", description = "Detalle actualizado exitosamente")
  public ResponseEntity<ReservationDetail> update(
      @Parameter(description = "ID del detalle", example = "1")
      @PathVariable Long detailId,
      @Parameter(description = "Nuevos dias de alquiler", example = "3")
      @RequestParam int rentalDays) {

    return ResponseEntity.ok(
        updateUseCase.execute(detailId, rentalDays));
  }

  /*
   * =========================
   * ACTUALIZAR CANTIDAD
   * =========================
   */
  @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
  @PutMapping("/{detailId}/quantity")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Actualizar cantidad", description = "Actualiza la cantidad de herramientas")
  @ApiResponse(responseCode = "200", description = "Cantidad actualizada exitosamente")
  public ResponseEntity<ReservationDetail> updateQuantity(
      @Parameter(description = "ID del detalle", example = "1")
      @PathVariable Long detailId,
      @Parameter(description = "Nueva cantidad", example = "2")
      @RequestParam int quantity) {

    return ResponseEntity.ok(
        updateQuantityUseCase.execute(detailId, quantity));
  }

  /*
   * =========================
   * ELIMINAR DETALLE
   * =========================
   */
  @PreAuthorize("hasAnyRole('ADMIN')")
  @DeleteMapping("/{detailId}")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Eliminar detalle de reserva", description = "Elimina un detalle de reserva")
  @ApiResponse(responseCode = "204", description = "Detalle eliminado exitosamente")
  public ResponseEntity<Void> delete(
      @Parameter(description = "ID del detalle", example = "1")
      @PathVariable Long detailId) {

    deleteUseCase.execute(detailId);
    return ResponseEntity.noContent().build();
  }
}
