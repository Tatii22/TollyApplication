package com.rentaherramientas.tolly.infrastructure.persistence.adapters.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.rentaherramientas.tolly.domain.model.Return;
import com.rentaherramientas.tolly.domain.model.ReturnStatus;
import com.rentaherramientas.tolly.domain.ports.ReturnRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ClientEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReturnEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReturnStatusEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ClientJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ReservationJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ReturnJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ReturnStatusJpaRepository;

@Component
public class ReturnRepositoryAdapter implements ReturnRepository {

    private final ReturnJpaRepository returnJpaRepository;
    private final ReservationJpaRepository reservationJpaRepository;
    private final ClientJpaRepository clientJpaRepository;
    private final ReturnStatusJpaRepository returnStatusJpaRepository;

    public ReturnRepositoryAdapter(
        ReturnJpaRepository returnJpaRepository,
        ReservationJpaRepository reservationJpaRepository,
        ClientJpaRepository clientJpaRepository,
        ReturnStatusJpaRepository returnStatusJpaRepository) {
        this.returnJpaRepository = returnJpaRepository;
        this.reservationJpaRepository = reservationJpaRepository;
        this.clientJpaRepository = clientJpaRepository;
        this.returnStatusJpaRepository = returnStatusJpaRepository;
    }

    @Override
    public Return save(Return value) {
        ReservationEntity reservationEntity = reservationJpaRepository
            .findById(value.getReservationId())
            .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));
        ClientEntity clientEntity = clientJpaRepository
            .findById(value.getClientId())
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        ReturnStatusEntity statusEntity = returnStatusJpaRepository
            .findById(value.getStatus().getId())
            .orElseThrow(() -> new IllegalArgumentException("Estado de devolucion no encontrado"));

        ReturnEntity entity = new ReturnEntity();
        entity.setId(value.getId());
        entity.setReservation(reservationEntity);
        entity.setClient(clientEntity);
        entity.setReturnDate(value.getReturnDate());
        entity.setReturnStatus(statusEntity);
        entity.setObservations(value.getObservations());

        ReturnEntity saved = returnJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Return> findById(Long id) {
        return returnJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Return> findAll() {
        return returnJpaRepository.findAll()
            .stream()
            .map(this::toDomain)
            .toList();
    }

    @Override
    public List<Return> findByReservationId(Long reservationId) {
        return returnJpaRepository.findByReservationId(reservationId)
            .stream()
            .map(this::toDomain)
            .toList();
    }

    @Override
    public void deleteById(Long id) {
        returnJpaRepository.deleteById(id);
    }

    private Return toDomain(ReturnEntity entity) {
        ReturnStatus status = null;
        if (entity.getReturnStatus() != null) {
            status = new ReturnStatus(
                entity.getReturnStatus().getId(),
                entity.getReturnStatus().getName());
        }

        return Return.reconstruct(
            entity.getId(),
            entity.getReservationId(),
            entity.getClientId(),
            entity.getReturnDate(),
            status,
            entity.getObservations());
    }
}
