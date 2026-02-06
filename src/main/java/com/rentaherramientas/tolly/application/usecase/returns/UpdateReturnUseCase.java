package com.rentaherramientas.tolly.application.usecase.returns;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.application.dto.returns.ReturnResponse;
import com.rentaherramientas.tolly.application.dto.returns.UpdateReturnRequest;
import com.rentaherramientas.tolly.application.mapper.ReturnMapper;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Return;
import com.rentaherramientas.tolly.domain.model.ReturnStatus;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ReturnRepository;
import com.rentaherramientas.tolly.domain.ports.ReturnStatusRepository;

@Service
public class UpdateReturnUseCase {

    private final ReturnRepository returnRepository;
    private final ReturnStatusRepository returnStatusRepository;
    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final ReturnMapper returnMapper;

    public UpdateReturnUseCase(
        ReturnRepository returnRepository,
        ReturnStatusRepository returnStatusRepository,
        ReservationRepository reservationRepository,
        ClientRepository clientRepository,
        ReturnMapper returnMapper) {
        this.returnRepository = returnRepository;
        this.returnStatusRepository = returnStatusRepository;
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
        this.returnMapper = returnMapper;
    }

    @Transactional
    public ReturnResponse execute(Long id, UpdateReturnRequest request) {
        Return existing = returnRepository.findById(id)
            .orElseThrow(() -> new DomainException("Devolucion no encontrada con ID: " + id));

        reservationRepository.findById(request.reservationId())
            .orElseThrow(() -> new DomainException("Reserva no encontrada con ID: " + request.reservationId()));
        clientRepository.findById(request.clientId())
            .orElseThrow(() -> new DomainException("Cliente no encontrado con ID: " + request.clientId()));

        ReturnStatus status = returnStatusRepository.findById(request.returnStatusId())
            .orElseThrow(() -> new DomainException("Estado de devolucion no encontrado con ID: " + request.returnStatusId()));

        existing.setReservationId(request.reservationId());
        existing.setClientId(request.clientId());
        existing.setReturnDate(request.returnDate());
        existing.setStatus(status);
        existing.setObservations(request.observations());

        Return saved = returnRepository.save(existing);
        return returnMapper.toReturnResponse(saved);
    }
}
