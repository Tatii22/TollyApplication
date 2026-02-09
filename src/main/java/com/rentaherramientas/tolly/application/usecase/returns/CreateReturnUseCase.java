package com.rentaherramientas.tolly.application.usecase.returns;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.application.dto.returns.CreateReturnRequest;
import com.rentaherramientas.tolly.application.dto.returns.ReturnDetailRequest;
import com.rentaherramientas.tolly.application.dto.returns.ReturnResponse;
import com.rentaherramientas.tolly.application.mapper.ReturnMapper;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.Return;
import com.rentaherramientas.tolly.domain.model.ReturnDetail;
import com.rentaherramientas.tolly.domain.model.ReturnStatus;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.model.ReservationDetail;
import com.rentaherramientas.tolly.domain.ports.ReservationDetailRepository;
import com.rentaherramientas.tolly.domain.ports.ReturnDetailRepository;
import com.rentaherramientas.tolly.domain.ports.ReturnRepository;
import com.rentaherramientas.tolly.domain.ports.ReturnStatusRepository;
import com.rentaherramientas.tolly.domain.ports.ToolRepository;

@Service
public class CreateReturnUseCase {

    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_CLIENT_DAMAGED = "CL_DAMAGED";
    private static final String STATUS_CLIENT_INCOMPLETE = "CL_INCOMPLETE";
    private static final String RESERVATION_IN_PROGRESS = "IN_PROGRESS";

    private final ReturnRepository returnRepository;
    private final ReturnStatusRepository returnStatusRepository;
    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final ReservationDetailRepository reservationDetailRepository;
    private final ReturnDetailRepository returnDetailRepository;
    private final ToolRepository toolRepository;
    private final ReturnMapper returnMapper;

    public CreateReturnUseCase(
        ReturnRepository returnRepository,
        ReturnStatusRepository returnStatusRepository,
        ReservationRepository reservationRepository,
        ClientRepository clientRepository,
        ReservationDetailRepository reservationDetailRepository,
        ReturnDetailRepository returnDetailRepository,
        ToolRepository toolRepository,
        ReturnMapper returnMapper) {
        this.returnRepository = returnRepository;
        this.returnStatusRepository = returnStatusRepository;
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
        this.reservationDetailRepository = reservationDetailRepository;
        this.returnDetailRepository = returnDetailRepository;
        this.toolRepository = toolRepository;
        this.returnMapper = returnMapper;
    }

    @Transactional
    public ReturnResponse execute(CreateReturnRequest request, java.util.UUID userId) {
        Reservation reservation = reservationRepository.findById(request.reservationId())
            .orElseThrow(() -> new DomainException("Reserva no encontrada con ID: " + request.reservationId()));
        if (userId == null) {
            throw new DomainException("Usuario no autenticado");
        }
        var client = clientRepository.findByUserId(User.restore(userId))
            .orElseThrow(() -> new DomainException("Cliente no encontrado para el usuario"));

        if (reservation.getStatus() == null
            || !RESERVATION_IN_PROGRESS.equalsIgnoreCase(reservation.getStatus().getName())) {
            throw new DomainException("La reserva no esta en estado IN_PROGRESS");
        }

        if (!client.getId().equals(reservation.getClientId())) {
            throw new DomainException("La reserva no pertenece al cliente");
        }

        String requestedStatus = normalizeStatus(request.returnStatusName());
        String initialStatus = resolveInitialStatus(requestedStatus);

        ReturnStatus status = returnStatusRepository.findByName(initialStatus)
            .orElseThrow(() -> new DomainException("Estado de devolucion no encontrado: " + initialStatus));

        Return value = returnMapper.toReturn(request);
        value.setClientId(client.getId());
        value.setStatus(status);

        Return saved = returnRepository.save(value);
        saveDetails(saved, request.details());
        return returnMapper.toReturnResponse(saved);
    }

    private void saveDetails(Return saved, java.util.List<ReturnDetailRequest> details) {
        java.util.Map<Long, Integer> reservedCounts = loadReservedCounts(saved.getReservationId());
        for (ReturnDetailRequest detail : details) {
            if (!reservationDetailRepository.existsByReservationIdAndToolId(
                saved.getReservationId(),
                detail.toolId())) {
                throw new DomainException("La herramienta no pertenece a la reserva: " + detail.toolId());
            }

            validateQuantity(detail, reservedCounts);

            if (returnDetailRepository.existsByReturnIdAndToolId(saved.getId(), detail.toolId())) {
                throw new DomainException("La herramienta ya fue registrada en esta devolucion: " + detail.toolId());
            }

            var tool = toolRepository.findById(detail.toolId())
                .orElseThrow(() -> new DomainException("Herramienta no encontrada con ID: " + detail.toolId()));

            ReturnDetail returnDetail = ReturnDetail.create(
                saved,
                tool,
                detail.quantity(),
                detail.observations()
            );
            returnDetailRepository.save(returnDetail);
        }
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

    private void validateQuantity(ReturnDetailRequest detail, java.util.Map<Long, Integer> reservedCounts) {
        int reserved = reservedCounts.getOrDefault(detail.toolId(), 0);
        if (reserved < 1) {
            throw new DomainException("La herramienta no tiene unidades reservadas: " + detail.toolId());
        }
        if (detail.quantity() > reserved) {
            throw new DomainException("La cantidad excede lo reservado para la herramienta: " + detail.toolId());
        }
    }

    private String normalizeStatus(String statusName) {
        if (statusName == null) return "";
        return statusName.trim().toUpperCase();
    }

    private String resolveInitialStatus(String requestedStatus) {
        if (requestedStatus.isBlank()) {
            return STATUS_PENDING;
        }
        if (!STATUS_CLIENT_DAMAGED.equals(requestedStatus) && !STATUS_CLIENT_INCOMPLETE.equals(requestedStatus)) {
            throw new DomainException("Estado de devolucion invalido para cliente: " + requestedStatus);
        }
        return requestedStatus;
    }
}
