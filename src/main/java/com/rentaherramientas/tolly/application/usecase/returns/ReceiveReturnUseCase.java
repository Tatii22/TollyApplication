package com.rentaherramientas.tolly.application.usecase.returns;

import java.util.List;
import java.util.UUID;

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
import com.rentaherramientas.tolly.domain.model.Supplier;
import com.rentaherramientas.tolly.domain.model.Tool;
import com.rentaherramientas.tolly.domain.model.ToolStatus;
import com.rentaherramientas.tolly.domain.ports.ReservationDetailRepository;
import com.rentaherramientas.tolly.domain.ports.ReturnDetailRepository;
import com.rentaherramientas.tolly.domain.ports.ReturnRepository;
import com.rentaherramientas.tolly.domain.ports.ReturnStatusRepository;
import com.rentaherramientas.tolly.domain.ports.SupplierRepository;
import com.rentaherramientas.tolly.domain.ports.ToolRepository;
import com.rentaherramientas.tolly.domain.ports.ToolStatusRepository;

@Service
public class ReceiveReturnUseCase {

    private static final String STATUS_SENT = "SENT";
    private static final String STATUS_RECEIVED = "RECEIVED";
    private static final String STATUS_DAMAGED = "DAMAGED";

    private static final String TOOL_AVAILABLE = "AVAILABLE";
    private static final String TOOL_UNDER_REPAIR = "UNDER_REPAIR";

    private final ReturnRepository returnRepository;
    private final ReturnStatusRepository returnStatusRepository;
    private final ReturnDetailRepository returnDetailRepository;
    private final ReservationDetailRepository reservationDetailRepository;
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

        if (existing.getStatus() == null
            || !STATUS_SENT.equalsIgnoreCase(existing.getStatus().getName())) {
            throw new DomainException("Solo se puede recibir devolucion en estado SENT");
        }

        String statusName = normalizeStatus(request.returnStatusName());
        if (!STATUS_RECEIVED.equals(statusName) && !STATUS_DAMAGED.equals(statusName)) {
            throw new DomainException("Estado de devolucion invalido: " + request.returnStatusName());
        }

        if (!isAdmin) {
            validateSupplierOwnership(existing.getReservationId(), request.details(), userId);
        } else {
            validateToolsInReservation(existing.getReservationId(), request.details());
        }

        ReturnStatus newStatus = returnStatusRepository.findByName(statusName)
            .orElseThrow(() -> new DomainException("Estado de devolucion no encontrado: " + statusName));
        existing.setStatus(newStatus);

        if (request.observations() != null) {
            existing.setObservations(request.observations());
        }

        Return saved = returnRepository.save(existing);

        saveDetailsAndUpdateTools(saved, request.details(), statusName);

        if (STATUS_DAMAGED.equals(statusName)) {
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

        return returnMapper.toReturnResponse(saved);
    }

    private void saveDetailsAndUpdateTools(Return saved, List<ReturnDetailRequest> details, String statusName) {
        ToolStatus toolStatus = resolveToolStatus(statusName);

        for (ReturnDetailRequest detail : details) {
            Tool tool = toolRepository.findById(detail.toolId())
                .orElseThrow(() -> new DomainException("Herramienta no encontrada con ID: " + detail.toolId()));

            if (returnDetailRepository.existsByReturnIdAndToolId(saved.getId(), tool.getId())) {
                throw new DomainException("La herramienta ya fue registrada en esta devolucion: " + tool.getId());
            }

            ReturnDetail returnDetail = ReturnDetail.create(
                saved,
                tool,
                detail.quantity(),
                detail.observations()
            );
            returnDetailRepository.save(returnDetail);

            tool.setStatusId(toolStatus.getId());
            toolRepository.update(tool.getId(), tool)
                .orElseThrow(() -> new DomainException("No se pudo actualizar el estado de la herramienta: " + tool.getId()));
        }
    }

    private ToolStatus resolveToolStatus(String returnStatusName) {
        String toolStatusName = STATUS_DAMAGED.equals(returnStatusName) ? TOOL_UNDER_REPAIR : TOOL_AVAILABLE;
        return toolStatusRepository.findByName(toolStatusName)
            .orElseThrow(() -> new DomainException("Estado de herramienta no encontrado: " + toolStatusName));
    }

    private void validateSupplierOwnership(Long reservationId, List<ReturnDetailRequest> details, UUID userId) {
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
        }
    }

    private void validateToolsInReservation(Long reservationId, List<ReturnDetailRequest> details) {
        for (ReturnDetailRequest detail : details) {
            if (!reservationDetailRepository.existsByReservationIdAndToolId(reservationId, detail.toolId())) {
                throw new DomainException("La herramienta no pertenece a la reserva: " + detail.toolId());
            }
        }
    }

    private String normalizeStatus(String statusName) {
        if (statusName == null) return "";
        return statusName.trim().toUpperCase();
    }
}
