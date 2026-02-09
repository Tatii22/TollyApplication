package com.rentaherramientas.tolly.application.usecase.returns;

import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.application.dto.returns.ReceiveReturnRequest;
import com.rentaherramientas.tolly.application.dto.returns.ReturnDetailRequest;
import com.rentaherramientas.tolly.application.dto.returns.ReturnResponse;
import com.rentaherramientas.tolly.application.mapper.ReturnMapper;
import com.rentaherramientas.tolly.application.usecase.reservation.MarkReservationFinishedUseCase;
import com.rentaherramientas.tolly.application.usecase.reservation.MarkReservationIncidentUseCase;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Return;
import com.rentaherramientas.tolly.domain.model.ReturnDetail;
import com.rentaherramientas.tolly.domain.model.ReturnStatus;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.ReservationDetail;
import com.rentaherramientas.tolly.domain.model.Supplier;
import com.rentaherramientas.tolly.domain.model.Tool;
import com.rentaherramientas.tolly.domain.model.ToolStatus;
import com.rentaherramientas.tolly.domain.ports.ReservationDetailRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ReturnDetailRepository;
import com.rentaherramientas.tolly.domain.ports.ReturnRepository;
import com.rentaherramientas.tolly.domain.ports.ReturnStatusRepository;
import com.rentaherramientas.tolly.domain.ports.SupplierRepository;
import com.rentaherramientas.tolly.domain.ports.ToolRepository;
import com.rentaherramientas.tolly.domain.ports.ToolStatusRepository;

@Service
public class ReceiveReturnUseCase {

    private static final String STATUS_SENT = "SENT";
    private static final String STATUS_CLIENT_DAMAGED = "CL_DAMAGED";
    private static final String STATUS_CLIENT_INCOMPLETE = "CL_INCOMPLETE";
    private static final String STATUS_RECEIVED = "RECEIVED";
    private static final String STATUS_DAMAGED = "DAMAGED";
    private static final String STATUS_SUPPLIER_INCOMPLETE = "SPP_INCOMPLETE";

    private static final String TOOL_AVAILABLE = "AVAILABLE";
    private static final String TOOL_UNAVAILABLE = "UNAVAILABLE";
    private static final String TOOL_UNDER_REPAIR = "UNDER_REPAIR";

    private final ReturnRepository returnRepository;
    private final ReturnStatusRepository returnStatusRepository;
    private final ReturnDetailRepository returnDetailRepository;
    private final ReservationDetailRepository reservationDetailRepository;
    private final ReservationRepository reservationRepository;
    private final SupplierRepository supplierRepository;
    private final ToolRepository toolRepository;
    private final ToolStatusRepository toolStatusRepository;
    private final MarkReservationFinishedUseCase markReservationFinishedUseCase;
    private final MarkReservationIncidentUseCase markReservationIncidentUseCase;
    private final ReturnMapper returnMapper;

    public ReceiveReturnUseCase(
        ReturnRepository returnRepository,
        ReturnStatusRepository returnStatusRepository,
        ReturnDetailRepository returnDetailRepository,
        ReservationDetailRepository reservationDetailRepository,
        ReservationRepository reservationRepository,
        SupplierRepository supplierRepository,
        ToolRepository toolRepository,
        ToolStatusRepository toolStatusRepository,
        MarkReservationFinishedUseCase markReservationFinishedUseCase,
        MarkReservationIncidentUseCase markReservationIncidentUseCase,
        ReturnMapper returnMapper) {
        this.returnRepository = returnRepository;
        this.returnStatusRepository = returnStatusRepository;
        this.returnDetailRepository = returnDetailRepository;
        this.reservationDetailRepository = reservationDetailRepository;
        this.reservationRepository = reservationRepository;
        this.supplierRepository = supplierRepository;
        this.toolRepository = toolRepository;
        this.toolStatusRepository = toolStatusRepository;
        this.markReservationFinishedUseCase = markReservationFinishedUseCase;
        this.markReservationIncidentUseCase = markReservationIncidentUseCase;
        this.returnMapper = returnMapper;
    }

    @Transactional
    public ReturnResponse execute(Long returnId, ReceiveReturnRequest request, UUID userId, boolean isAdmin) {
        Return existing = returnRepository.findById(returnId)
            .orElseThrow(() -> new DomainException("Devolucion no encontrada con ID: " + returnId));

        if (existing.getStatus() == null) {
            throw new DomainException("Estado de devolucion no encontrado");
        }
        String currentStatus = existing.getStatus().getName();
        if (!STATUS_SENT.equalsIgnoreCase(currentStatus)
            && !STATUS_CLIENT_DAMAGED.equalsIgnoreCase(currentStatus)
            && !STATUS_CLIENT_INCOMPLETE.equalsIgnoreCase(currentStatus)) {
            throw new DomainException("Solo se puede recibir devolucion en estado SENT, CL_DAMAGED o CL_INCOMPLETE");
        }

        String statusName = normalizeStatus(request.returnStatusName());
        if (!STATUS_RECEIVED.equals(statusName)
            && !STATUS_DAMAGED.equals(statusName)
            && !STATUS_SUPPLIER_INCOMPLETE.equals(statusName)) {
            throw new DomainException("Estado de devolucion invalido: " + request.returnStatusName());
        }

        List<ReturnDetailRequest> details = resolveDetails(request.details(), existing.getId());

        java.util.Map<Long, Integer> reservedCounts = loadReservedCounts(existing.getReservationId());
        if (!isAdmin) {
            validateSupplierOwnership(existing.getReservationId(), details, userId, reservedCounts);
        } else {
            validateToolsInReservation(existing.getReservationId(), details, reservedCounts);
        }

        ReturnStatus newStatus = returnStatusRepository.findByName(statusName)
            .orElseThrow(() -> new DomainException("Estado de devolucion no encontrado: " + statusName));
        existing.setStatus(newStatus);

        if (request.observations() != null) {
            existing.setObservations(request.observations());
        }

        Return saved = returnRepository.save(existing);

        saveDetailsAndUpdateTools(saved, details, statusName);

        boolean allReturned = isReservationFullyReturned(saved.getReservationId());

        if (STATUS_DAMAGED.equals(statusName) && !allReturned) {
            handleDamagedReplacement(saved, details);
        }

        if (allReturned) {
            if (STATUS_DAMAGED.equals(statusName) || STATUS_SUPPLIER_INCOMPLETE.equals(statusName)) {
                markReservationIncidentUseCase.execute(
                    saved.getReservationId(),
                    isAdmin ? null : userId,
                    isAdmin
                );
            } else {
                markReservationFinishedUseCase.execute(
                    saved.getReservationId(),
                    isAdmin ? null : userId,
                    isAdmin
                );
            }
        }

        return returnMapper.toReturnResponse(saved);
    }

    private void saveDetailsAndUpdateTools(Return saved, List<ReturnDetailRequest> details, String statusName) {
        ToolStatus toolStatus = resolveToolStatus(statusName);

        for (ReturnDetailRequest detail : details) {
            Tool tool = toolRepository.findById(detail.toolId())
                .orElseThrow(() -> new DomainException("Herramienta no encontrada con ID: " + detail.toolId()));

            boolean alreadyRegistered = returnDetailRepository.existsByReturnIdAndToolId(saved.getId(), tool.getId());
            if (!alreadyRegistered) {
                ReturnDetail returnDetail = ReturnDetail.create(
                    saved,
                    tool,
                    detail.quantity(),
                    detail.observations()
                );
                returnDetailRepository.save(returnDetail);
            }

            tool.setStatusId(toolStatus.getId());
            toolRepository.update(tool.getId(), tool)
                .orElseThrow(() -> new DomainException("No se pudo actualizar el estado de la herramienta: " + tool.getId()));
        }
    }

    private ToolStatus resolveToolStatus(String returnStatusName) {
        String toolStatusName = (STATUS_DAMAGED.equals(returnStatusName) || STATUS_SUPPLIER_INCOMPLETE.equals(returnStatusName))
            ? TOOL_UNDER_REPAIR
            : TOOL_AVAILABLE;
        return toolStatusRepository.findByName(toolStatusName)
            .orElseThrow(() -> new DomainException("Estado de herramienta no encontrado: " + toolStatusName));
    }

    private void validateSupplierOwnership(
        Long reservationId,
        List<ReturnDetailRequest> details,
        UUID userId,
        java.util.Map<Long, Integer> reservedCounts) {
        if (userId == null) {
            throw new DomainException("Usuario no autenticado");
        }
        Supplier supplier = supplierRepository.findByUserId(userId)
            .orElseThrow(() -> new DomainException("Proveedor no encontrado para el usuario"));

        for (ReturnDetailRequest detail : details) {
            Tool tool = toolRepository.findById(detail.toolId())
                .orElseThrow(() -> new DomainException("Herramienta no encontrada con ID: " + detail.toolId()));

            if (tool.getSupplierId() == null || !tool.getSupplierId().equals(supplier.getId())) {
                throw new DomainException("La herramienta no pertenece al proveedor: " + tool.getId());
            }

            if (!reservationDetailRepository.existsByReservationIdAndToolId(reservationId, tool.getId())) {
                throw new DomainException("La herramienta no pertenece a la reserva: " + tool.getId());
            }

            validateQuantity(detail, reservedCounts);
        }
    }

    private void validateToolsInReservation(
        Long reservationId,
        List<ReturnDetailRequest> details,
        java.util.Map<Long, Integer> reservedCounts) {
        for (ReturnDetailRequest detail : details) {
            if (!reservationDetailRepository.existsByReservationIdAndToolId(reservationId, detail.toolId())) {
                throw new DomainException("La herramienta no pertenece a la reserva: " + detail.toolId());
            }
            validateQuantity(detail, reservedCounts);
        }
    }

    private String normalizeStatus(String statusName) {
        if (statusName == null) return "";
        return statusName.trim().toUpperCase();
    }

    private java.util.Map<Long, Integer> loadReservedCounts(Long reservationId) {
        java.util.Map<Long, Integer> counts = new java.util.HashMap<>();
        for (ReservationDetail detail : reservationDetailRepository.findByReservationId(reservationId)) {
            if (detail.getTool() == null || detail.getTool().getId() == null) {
                continue;
            }
            Long toolId = detail.getTool().getId();
            counts.put(toolId, counts.getOrDefault(toolId, 0) + detail.getQuantity());
        }
        return counts;
    }

    private boolean isReservationFullyReturned(Long reservationId) {
        Map<Long, Integer> reservedCounts = loadReservedCounts(reservationId);
        Map<Long, Integer> returnedCounts = loadReturnedCounts(reservationId);
        for (Map.Entry<Long, Integer> reserved : reservedCounts.entrySet()) {
            int returned = returnedCounts.getOrDefault(reserved.getKey(), 0);
            if (returned < reserved.getValue()) {
                return false;
            }
        }
        return true;
    }

    private Map<Long, Integer> loadReturnedCounts(Long reservationId) {
        Map<Long, Integer> counts = new HashMap<>();
        List<Return> returns = returnRepository.findByReservationId(reservationId);
        for (Return value : returns) {
            List<ReturnDetail> details = returnDetailRepository.findByReturnId(value.getId());
            for (ReturnDetail detail : details) {
                if (detail.getTool() == null || detail.getTool().getId() == null) {
                    continue;
                }
                Long toolId = detail.getTool().getId();
                counts.put(toolId, counts.getOrDefault(toolId, 0) + detail.getQuantity());
            }
        }
        return counts;
    }

    private void handleDamagedReplacement(Return saved, List<ReturnDetailRequest> details) {
        Reservation reservation = reservationRepository.findById(saved.getReservationId())
            .orElseThrow(() -> new DomainException("Reserva no encontrada con ID: " + saved.getReservationId()));

        int rentalDays = calculateRemainingDays(reservation.getStartDate(), reservation.getEndDate());

        for (ReturnDetailRequest detail : details) {
            Tool replacement = resolveReplacementTool(detail.toolId(), detail.quantity());
            if (replacement == null) {
                throw new DomainException("No hay herramienta disponible para reemplazo del toolId: " + detail.toolId());
            }

            ReservationDetail replacementDetail = ReservationDetail.reconstruct(
                null,
                replacement,
                reservation,
                replacement.getDailyPrice(),
                rentalDays,
                detail.quantity(),
                java.math.BigDecimal.ZERO
            );

            reservationDetailRepository.save(replacementDetail);
            adjustToolAvailability(replacement, detail.quantity());
        }
    }

    private int calculateRemainingDays(LocalDate startDate, LocalDate endDate) {
        LocalDate from = LocalDate.now();
        if (from.isBefore(startDate)) {
            from = startDate;
        }
        long days = ChronoUnit.DAYS.between(from, endDate) + 1;
        return (int) Math.max(1, days);
    }

    private Tool resolveReplacementTool(Long originalToolId, int quantity) {
        Tool original = toolRepository.findById(originalToolId)
            .orElseThrow(() -> new DomainException("Herramienta no encontrada con ID: " + originalToolId));

        Integer available = original.getAvailableQuantity();
        if (available != null && available >= quantity) {
            return original;
        }

        if (original.getCategoryId() == null) {
            return null;
        }

        List<Tool> alternatives = toolRepository.findByFilters(
            original.getCategoryId(),
            TOOL_AVAILABLE,
            quantity,
            null,
            null
        );

        return alternatives.isEmpty() ? null : alternatives.get(0);
    }

    private void adjustToolAvailability(Tool tool, int quantity) {
        Integer available = tool.getAvailableQuantity();
        if (available == null) {
            Integer total = tool.getTotalQuantity();
            if (total == null) {
                throw new DomainException("La herramienta no tiene cantidad total definida");
            }
            available = total;
        }

        int updated = available - quantity;
        if (updated < 0) {
            throw new DomainException("La cantidad disponible no puede ser negativa");
        }

        tool.setAvailableQuantity(updated);
        toolRepository.update(tool.getId(), tool)
            .orElseThrow(() -> new DomainException("No se pudo actualizar la herramienta: " + tool.getId()));
    }

    private void validateQuantity(ReturnDetailRequest detail, java.util.Map<Long, Integer> reservedCounts) {
        int reserved = reservedCounts.getOrDefault(detail.toolId(), 0);
        if (reserved < 1) {
            throw new DomainException("La herramienta no tiene unidades reservadas: " + detail.toolId());
        }
        if (detail.quantity() > reserved) {
            throw new DomainException("La cantidad excede lo reservado para la herramienta: " + detail.toolId());
        }
    }

    private List<ReturnDetailRequest> resolveDetails(List<ReturnDetailRequest> details, Long returnId) {
        if (details != null && !details.isEmpty()) {
            return details;
        }
        List<ReturnDetail> existingDetails = returnDetailRepository.findByReturnId(returnId);
        if (existingDetails.isEmpty()) {
            throw new DomainException("Debe incluir al menos un detalle de devolucion");
        }
        List<ReturnDetailRequest> resolved = new ArrayList<>();
        for (ReturnDetail detail : existingDetails) {
            if (detail.getTool() == null || detail.getTool().getId() == null) {
                continue;
            }
            resolved.add(new ReturnDetailRequest(
                detail.getTool().getId(),
                detail.getQuantity(),
                detail.getObservations()
            ));
        }
        if (resolved.isEmpty()) {
            throw new DomainException("Debe incluir al menos un detalle de devolucion");
        }
        return resolved;
    }
}
