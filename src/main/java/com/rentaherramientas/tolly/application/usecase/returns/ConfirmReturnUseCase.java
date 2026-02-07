package com.rentaherramientas.tolly.application.usecase.returns;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.application.dto.returns.ReturnResponse;
import com.rentaherramientas.tolly.application.mapper.ReturnMapper;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Client;
import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.Return;
import com.rentaherramientas.tolly.domain.model.ReturnStatus;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ReturnRepository;
import com.rentaherramientas.tolly.domain.ports.ReturnStatusRepository;
import com.rentaherramientas.tolly.domain.ports.UserRepository;

@Service
public class ConfirmReturnUseCase {

    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_SENT = "SENT";
    private static final String RESERVATION_IN_PROGRESS = "IN_PROGRESS";

    private final ReturnRepository returnRepository;
    private final ReturnStatusRepository returnStatusRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final ReturnMapper returnMapper;

    public ConfirmReturnUseCase(
        ReturnRepository returnRepository,
        ReturnStatusRepository returnStatusRepository,
        ReservationRepository reservationRepository,
        UserRepository userRepository,
        ClientRepository clientRepository,
        ReturnMapper returnMapper) {
        this.returnRepository = returnRepository;
        this.returnStatusRepository = returnStatusRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.returnMapper = returnMapper;
    }

    @Transactional
    public ReturnResponse execute(Long returnId, UUID userId) {
        Return existing = returnRepository.findById(returnId)
            .orElseThrow(() -> new DomainException("Devolucion no encontrada con ID: " + returnId));

        Reservation reservation = reservationRepository.findById(existing.getReservationId())
            .orElseThrow(() -> new DomainException("Reserva no encontrada con ID: " + existing.getReservationId()));

        if (reservation.getStatus() == null
            || !RESERVATION_IN_PROGRESS.equalsIgnoreCase(reservation.getStatus().getName())) {
            throw new DomainException("La reserva no esta en estado IN_PROGRESS");
        }

        if (existing.getStatus() == null
            || !STATUS_PENDING.equalsIgnoreCase(existing.getStatus().getName())) {
            throw new DomainException("Solo se puede confirmar devolucion en estado PENDING");
        }

        if (userId == null) {
            throw new DomainException("Usuario no autenticado");
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new DomainException("Usuario no encontrado"));
        Client client = clientRepository.findByUserId(user)
            .orElseThrow(() -> new DomainException("Cliente no encontrado para el usuario"));

        if (!client.getId().equals(existing.getClientId())) {
            throw new DomainException("No tienes permiso para confirmar esta devolucion");
        }

        ReturnStatus sentStatus = returnStatusRepository.findByName(STATUS_SENT)
            .orElseThrow(() -> new DomainException("Estado SENT no encontrado"));
        existing.setStatus(sentStatus);

        Return saved = returnRepository.save(existing);
        return returnMapper.toReturnResponse(saved);
    }
}
