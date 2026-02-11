package com.rentaherramientas.tolly.application.usecase.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.application.dto.reservation.ReservationResponse;
import com.rentaherramientas.tolly.domain.model.Payment;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.ReservationDetail;
import com.rentaherramientas.tolly.domain.model.ReservationStatus;
import com.rentaherramientas.tolly.domain.model.Supplier;
import com.rentaherramientas.tolly.domain.ports.PaymentRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationDetailRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationStatusRepository;
import com.rentaherramientas.tolly.domain.ports.SupplierRepository;

@Service
public class MarkReservationIncidentUseCase {

    private final ReservationRepository reservationRepository;
    private final ReservationStatusRepository reservationStatusRepository;
    private final PaymentRepository paymentRepository;
    private final ReservationDetailRepository reservationDetailRepository;
    private final SupplierRepository supplierRepository;

    public MarkReservationIncidentUseCase(ReservationRepository reservationRepository,
                                          ReservationStatusRepository reservationStatusRepository,
                                          PaymentRepository paymentRepository,
                                          ReservationDetailRepository reservationDetailRepository,
                                          SupplierRepository supplierRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationStatusRepository = reservationStatusRepository;
        this.paymentRepository = paymentRepository;
        this.reservationDetailRepository = reservationDetailRepository;
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public ReservationResponse execute(Long reservationId, UUID userId, boolean isAdmin) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con ID: " + reservationId));

        if (!"PENDIENTE".equalsIgnoreCase(reservation.getStatus().getName())) {
            throw new IllegalStateException("Solo se puede marcar incidencia en reservas PENDIENTE");
        }
        if (!LocalDate.now().isAfter(reservation.getEndDate())) {
            throw new IllegalStateException("La reserva aun no ha vencido");
        }

        Payment payment = paymentRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado para la reserva"));
        if (!payment.isPaid()) {
            throw new IllegalStateException("No se puede marcar incidencia con pago PENDING");
        }

        if (!isAdmin) {
            validateSupplierOwnership(reservationId, userId);
        }

        ReservationStatus incidentStatus = reservationStatusRepository.findByStatusName("IN_INCIDENT")
                .orElseThrow(() -> new IllegalArgumentException("Estado IN_INCIDENT no encontrado en la base de datos"));
        reservation.setStatus(incidentStatus);

        Reservation updated = reservationRepository.save(reservation);

        return new ReservationResponse(
                updated.getId(),
                updated.getClientId(),
                updated.getStartDate(),
                updated.getEndDate(),
                updated.getTotal(),
                updated.getStatus().getName(),
                updated.getCreatedAt()
        );
    }

    private void validateSupplierOwnership(Long reservationId, UUID userId) {
        if (userId == null) {
            throw new IllegalStateException("Usuario no autenticado");
        }
        Supplier supplier = supplierRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Proveedor no encontrado para el usuario"));
        List<ReservationDetail> details = reservationDetailRepository.findByReservationId(reservationId);
        if (details.isEmpty()) {
            throw new IllegalStateException("La reserva no tiene detalles asociados");
        }
        boolean ownsAll = details.stream()
                .allMatch(detail -> detail.getTool() != null
                        && detail.getTool().getSupplierId() != null
                        && detail.getTool().getSupplierId().equals(supplier.getId()));
        if (!ownsAll) {
            throw new IllegalStateException("La reserva no pertenece al proveedor");
        }
    }
}
