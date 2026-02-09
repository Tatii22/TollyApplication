package com.rentaherramientas.tolly.application.usecase.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.application.dto.reservation.ReservationResponse;
import java.util.List;
import java.util.UUID;

import com.rentaherramientas.tolly.domain.model.Payment;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.ReservationDetail;
import com.rentaherramientas.tolly.domain.model.ReservationStatus;
import com.rentaherramientas.tolly.domain.model.Supplier;
import com.rentaherramientas.tolly.domain.model.Tool;
import com.rentaherramientas.tolly.domain.ports.PaymentRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationDetailRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationStatusRepository;
import com.rentaherramientas.tolly.domain.ports.SupplierRepository;
import com.rentaherramientas.tolly.domain.ports.ToolRepository;

@Service
public class MarkReservationFinishedUseCase {

    private final ReservationRepository reservationRepository;
    private final ReservationStatusRepository reservationStatusRepository;
    private final PaymentRepository paymentRepository;
    private final ReservationDetailRepository reservationDetailRepository;
    private final SupplierRepository supplierRepository;
    private final ToolRepository toolRepository;

    public MarkReservationFinishedUseCase(ReservationRepository reservationRepository,
                                          ReservationStatusRepository reservationStatusRepository,
                                          PaymentRepository paymentRepository,
                                          ReservationDetailRepository reservationDetailRepository,
                                          SupplierRepository supplierRepository,
                                          ToolRepository toolRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationStatusRepository = reservationStatusRepository;
        this.paymentRepository = paymentRepository;
        this.reservationDetailRepository = reservationDetailRepository;
        this.supplierRepository = supplierRepository;
        this.toolRepository = toolRepository;
    }

    @Transactional
    public ReservationResponse execute(Long reservationId, UUID userId, boolean isAdmin) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con ID: " + reservationId));

        if (!"IN_PROGRESS".equalsIgnoreCase(reservation.getStatus().getName())
                && !"IN_INCIDENT".equalsIgnoreCase(reservation.getStatus().getName())) {
            throw new IllegalStateException("Solo se puede finalizar una reserva IN_PROGRESS o IN_INCIDENT");
        }

        Payment payment = paymentRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado para la reserva"));
        if (!payment.isPaid()) {
            throw new IllegalStateException("No se puede finalizar la reserva con pago PENDING");
        }

        if (!isAdmin) {
            validateSupplierOwnership(reservationId, userId);
        }

        ReservationStatus finishedStatus = reservationStatusRepository.findByStatusName("FINISHED")
                .orElseThrow(() -> new IllegalArgumentException("Estado FINISHED no encontrado en la base de datos"));
        reservation.setStatus(finishedStatus);

        List<ReservationDetail> details = reservationDetailRepository.findByReservationId(reservationId);
        restoreAvailability(details);

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

    private void restoreAvailability(List<ReservationDetail> details) {
        for (ReservationDetail detail : details) {
            Tool tool = detail.getTool();
            if (tool == null || tool.getId() == null) {
                continue;
            }
            Integer available = tool.getAvailableQuantity();
            if (available == null) {
                Integer total = tool.getTotalQuantity();
                if (total == null) {
                    continue;
                }
                available = total;
            }
            int updated = available + detail.getQuantity();
            tool.setAvailableQuantity(updated);
            toolRepository.update(tool.getId(), tool);
        }
    }
}
