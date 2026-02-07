package com.rentaherramientas.tolly.application.usecase.returns;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.application.dto.returns.CreateReturnRequest;
import com.rentaherramientas.tolly.application.dto.returns.ReturnResponse;
import com.rentaherramientas.tolly.application.mapper.ReturnMapper;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.Return;
import com.rentaherramientas.tolly.domain.model.ReturnStatus;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ReturnRepository;
import com.rentaherramientas.tolly.domain.ports.ReturnStatusRepository;

@Service
public class CreateReturnUseCase {

    private static final String STATUS_PENDING = "PENDING";
    private static final String RESERVATION_IN_PROGRESS = "IN_PROGRESS";

    private final ReturnRepository returnRepository;
    private final ReturnStatusRepository returnStatusRepository;
    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final ReturnMapper returnMapper;

    public CreateReturnUseCase(
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
    public ReturnResponse execute(CreateReturnRequest request) {
        Reservation reservation = reservationRepository.findById(request.reservationId())
            .orElseThrow(() -> new DomainException("Reserva no encontrada con ID: " + request.reservationId()));
        clientRepository.findById(request.clientId())
            .orElseThrow(() -> new DomainException("Cliente no encontrado con ID: " + request.clientId()));

        if (reservation.getStatus() == null
            || !RESERVATION_IN_PROGRESS.equalsIgnoreCase(reservation.getStatus().getName())) {
            throw new DomainException("La reserva no esta en estado IN_PROGRESS");
        }

        ReturnStatus status = returnStatusRepository.findByName(STATUS_PENDING)
            .orElseThrow(() -> new DomainException("Estado PENDING no encontrado"));

        Return value = returnMapper.toReturn(request);
        value.setStatus(status);

        Return saved = returnRepository.save(value);
        return returnMapper.toReturnResponse(saved);
    }
}
