package com.rentaherramientas.tolly.infrastructure.persistence.adapters.out.persistence;


import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ClientEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationStatusEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ClientJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ReservationJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ReservationStatusJpaRespository;
import com.rentaherramientas.tolly.application.mapper.ReservationMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
@Component
public class ReservationRepositoryAdapter implements ReservationRepository {

  private final ReservationJpaRepository jpaRepository;
  private final ClientJpaRepository clientJpaRepository;
  private final ReservationStatusJpaRespository reservationStatusJpaRepository;



    public ReservationRepositoryAdapter(
        ReservationJpaRepository jpaRepository,
        ClientJpaRepository clientJpaRepository,
        ReservationStatusJpaRespository reservationStatusJpaRepository) {
    this.jpaRepository = jpaRepository;
    this.clientJpaRepository = clientJpaRepository;
    this.reservationStatusJpaRepository = reservationStatusJpaRepository;
  }

    @Override
    public Reservation save(Reservation reservation) {
        ClientEntity clientEntity = clientJpaRepository
            .findById(reservation.getClientId())
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        ReservationStatusEntity statusEntity = reservationStatusJpaRepository
            .findById(reservation.getStatus().getId())
            .orElseThrow(() -> new IllegalArgumentException("Estado de reserva no encontrado"));
        ReservationEntity entity = ReservationMapper.toEntity(reservation, clientEntity, statusEntity);
        ReservationEntity saved = jpaRepository.save(entity);
        return ReservationMapper.toDomain(saved);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return jpaRepository.findById(id)
                .map(ReservationMapper::toDomain);
    }

    @Override
    public void delete(Reservation reservation) {
        ClientEntity clientEntity = clientJpaRepository
            .findById(reservation.getClientId())
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado")); // Deberías obtener el ClientEntity correspondiente
        ReservationStatusEntity statusEntity = reservationStatusJpaRepository
            .findById(reservation.getStatus().getId())
            .orElseThrow(() -> new IllegalArgumentException("Estado de reserva no encontrado"));
        ReservationEntity entity = ReservationMapper.toEntity(reservation, clientEntity, statusEntity);
        jpaRepository.delete(entity);
    }

    @Override
    public List<Reservation> findAll() {
        return jpaRepository.findAll().stream()
                .map(ReservationMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findByClientId(Long clientId) {
        return jpaRepository.findByClient_Id(clientId).stream()
                .map(ReservationMapper::toDomain)
                .collect(Collectors.toList());
    }

}

