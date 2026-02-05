package com.rentaherramientas.tolly.infrastructure.persistence.adapters.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.model.ReservationStatus;
import com.rentaherramientas.tolly.domain.ports.ReservationStatusRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationStatusEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ReservationStatusJpaRespository;
import com.rentaherramientas.tolly.application.mapper.ReservationStatusMapper;


@Repository
@Transactional
public class ReservationStatusRepositoryAdapter implements ReservationStatusRepository {

  private final ReservationStatusJpaRespository jpaRepository;

  public ReservationStatusRepositoryAdapter(ReservationStatusJpaRespository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

  @Override
  public ReservationStatus save(ReservationStatus reservationStatus) {
    ReservationStatusEntity entity = ReservationStatusMapper.toEntity(reservationStatus);
    ReservationStatusEntity savedEntity = jpaRepository.save(entity);
    return ReservationStatusMapper.toDomain(savedEntity);
  }

  @Override
  public Optional<ReservationStatus> findByStatusName(String name) {
    return jpaRepository.findByStatusName(name)
        .map(ReservationStatusMapper::toDomain);
  }

  @Override
  public Optional<ReservationStatus> findById (UUID id) {
    return jpaRepository.findById(id)
        .map(ReservationStatusMapper::toDomain);
  }

  @Override
  public List<ReservationStatus> findAll() {
    return jpaRepository.findAll()
        .stream()
        .map(ReservationStatusMapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public void delete(ReservationStatus reservationStatus) {
    ReservationStatusEntity entity = ReservationStatusMapper.toEntity(reservationStatus);
    jpaRepository.delete(entity);
  }
}
