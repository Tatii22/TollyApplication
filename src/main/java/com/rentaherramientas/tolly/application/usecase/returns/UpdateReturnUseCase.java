package com.rentaherramientas.tolly.application.usecase.returns;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.application.dto.returns.ReturnResponse;
import com.rentaherramientas.tolly.application.dto.returns.UpdateReturnRequest;
import com.rentaherramientas.tolly.application.mapper.ReturnMapper;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Return;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ReturnRepository;

@Service
public class UpdateReturnUseCase {

    private final ReturnRepository returnRepository;
    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final ReturnMapper returnMapper;

    public UpdateReturnUseCase(
        ReturnRepository returnRepository,
        ReservationRepository reservationRepository,
        ClientRepository clientRepository,
        ReturnMapper returnMapper) {
        this.returnRepository = returnRepository;
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
        this.returnMapper = returnMapper;
    }

    @Transactional
    public ReturnResponse execute(Long id, UpdateReturnRequest request) {
        Return existing = returnRepository.findById(id)
            .orElseThrow(() -> new DomainException("Devolucion no encontrada con ID: " + id));

        if (!existing.getReservationId().equals(request.reservationId())) {
            throw new DomainException("No se permite cambiar la reserva de una devolucion");
        }
        if (!existing.getClientId().equals(request.clientId())) {
            throw new DomainException("No se permite cambiar el cliente de una devolucion");
        }

        reservationRepository.findById(existing.getReservationId())
            .orElseThrow(() -> new DomainException("Reserva no encontrada con ID: " + existing.getReservationId()));
        clientRepository.findById(existing.getClientId())
            .orElseThrow(() -> new DomainException("Cliente no encontrado con ID: " + existing.getClientId()));

        if (existing.getStatus() == null || request.returnStatusId() == null
            || !existing.getStatus().getId().equals(request.returnStatusId())) {
            throw new DomainException("No se permite cambiar el estado en este endpoint");
        }

        existing.setReturnDate(request.returnDate());
        existing.setObservations(request.observations());

        Return saved = returnRepository.save(existing);
        return returnMapper.toReturnResponse(saved);
    }
}
