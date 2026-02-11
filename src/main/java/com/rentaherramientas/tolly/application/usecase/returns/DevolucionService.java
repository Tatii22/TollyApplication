package com.rentaherramientas.tolly.application.usecase.returns;

import java.util.List;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.application.dto.returns.DevolucionDTO;
import com.rentaherramientas.tolly.application.dto.returns.ReceiveReturnRequest;
import com.rentaherramientas.tolly.application.dto.returns.ReturnDetailRequest;
import com.rentaherramientas.tolly.application.dto.returns.ReturnResponse;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Return;
import com.rentaherramientas.tolly.domain.model.ReturnStatus;

public class DevolucionService {
  
  /*
  Lógica en DevolucionService (o ReservaService):
  Implementa un método registrarDevolucion(Long idReserva, DevolucionDTO dto).
  Busca la reserva.
  Si dto.estado es "OK":
  Cambia el estado de la Reserva a DEVUELTO_OK.
  Si dto.estado es "DAÑOS":
  Cambia el estado de la Reserva a DEVUELTO_CON_DAÑOS.
  Crea y guarda un nuevo objeto ReporteDano usando los datos del DTO y asociándolo a la reserva.
  */
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


}
