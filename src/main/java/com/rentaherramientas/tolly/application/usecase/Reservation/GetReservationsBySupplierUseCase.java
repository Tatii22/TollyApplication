package com.rentaherramientas.tolly.application.usecase.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rentaherramientas.tolly.application.dto.reservation.ReservationResponse;
import com.rentaherramientas.tolly.domain.model.Supplier;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.SupplierRepository;

@Service
public class GetReservationsBySupplierUseCase {

  private final ReservationRepository reservationRepository;
  private final SupplierRepository supplierRepository;

  public GetReservationsBySupplierUseCase(
      ReservationRepository reservationRepository,
      SupplierRepository supplierRepository) {
    this.reservationRepository = reservationRepository;
    this.supplierRepository = supplierRepository;
  }

  public Page<ReservationResponse> execute(
      Long supplierId,
      String statusName,
      LocalDate from,
      LocalDate to,
      Pageable pageable,
      UUID userId,
      boolean isAdmin) {

    if (supplierId == null) {
      throw new IllegalArgumentException("SupplierId is required");
    }

    if (!isAdmin) {
      Supplier supplier = supplierRepository.findByUserId(userId)
          .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado para el usuario"));
      if (!supplier.getId().equals(supplierId)) {
        throw new IllegalStateException("No tiene permiso para ver reservas de otro proveedor");
      }
    }

    return reservationRepository.findBySupplierIdAndFilters(supplierId, statusName, from, to, pageable)
        .map(this::mapToResponse);
  }

  private ReservationResponse mapToResponse(Reservation reservation) {
    return new ReservationResponse(
        reservation.getId(),
        reservation.getClientId(),
        reservation.getStartDate(),
        reservation.getEndDate(),
        reservation.getTotal(),
        reservation.getStatus().getName(),
        reservation.getCreatedAt()
    );
  }
}
