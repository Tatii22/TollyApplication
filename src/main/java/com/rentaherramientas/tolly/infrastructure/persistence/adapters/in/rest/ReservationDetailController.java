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

@RestController
@RequestMapping("/api/reservations/details")
public class ReservationDetailController {

  private final CreateReservationDetailUseCase createUseCase;
  private final UpdateReservationDetailUseCase updateUseCase;
  private final DeleteReservationDetailUseCase deleteUseCase;
  private final GetReservationDetailsByReservationUseCase getByReservationUseCase;

  public ReservationDetailController(
      CreateReservationDetailUseCase createUseCase,
      UpdateReservationDetailUseCase updateUseCase,
      DeleteReservationDetailUseCase deleteUseCase,
      GetReservationDetailsByReservationUseCase getByReservationUseCase) {

    this.createUseCase = createUseCase;
    this.updateUseCase = updateUseCase;
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
  public ResponseEntity<ReservationDetail> create(
      @RequestParam Long reservationId,
      @RequestParam Long toolId) {

    ReservationDetail detail = createUseCase.execute(reservationId, toolId);

    return ResponseEntity.status(HttpStatus.CREATED).body(detail);
  }

  /*
   * =========================
   * LISTAR DETALLES DE UNA RESERVA
   * =========================
   */
  @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
  @GetMapping("/reservation/{reservationId}")
  public ResponseEntity<List<ReservationDetail>> getByReservation(
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
  public ResponseEntity<ReservationDetail> update(
      @PathVariable Long detailId,
      @RequestParam int rentalDays) {

    return ResponseEntity.ok(
        updateUseCase.execute(detailId, rentalDays));
  }

  /*
   * =========================
   * ELIMINAR DETALLE
   * =========================
   */
  @PreAuthorize("hasAnyRole('ADMIN')")
  @DeleteMapping("/{detailId}")
  public ResponseEntity<Void> delete(
      @PathVariable Long detailId) {

    deleteUseCase.execute(detailId);
    return ResponseEntity.noContent().build();
  }
}
