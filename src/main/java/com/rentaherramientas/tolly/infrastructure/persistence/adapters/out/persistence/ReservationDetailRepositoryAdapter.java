package com.rentaherramientas.tolly.infrastructure.persistence.adapters.out.persistence;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.rentaherramientas.tolly.application.mapper.ReservationDetailMapper;
import com.rentaherramientas.tolly.domain.model.ReservationDetail;
import com.rentaherramientas.tolly.domain.ports.ReservationDetailRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReservationDetailEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ReservationDetailJpaRepository;

@Repository
public class ReservationDetailRepositoryAdapter
    implements ReservationDetailRepository {

  private final ReservationDetailJpaRepository jpaRepository;

  public ReservationDetailRepositoryAdapter(
      ReservationDetailJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  /*
   * =========================
   * CREATE / UPDATE
   * =========================
   */
  @Override
  public ReservationDetail save(ReservationDetail reservationDetail) {
    ReservationDetailEntity entity =
        ReservationDetailMapper.toEntity(reservationDetail);

    ReservationDetailEntity saved =
        jpaRepository.save(entity);

    return ReservationDetailMapper.toDomain(saved);
  }

  /*
   * =========================
   * READ
   * =========================
   */
  @Override
  public Optional<ReservationDetail> findById(BigDecimal id) {
    return jpaRepository.findById(id)
        .map(ReservationDetailMapper::toDomain);
  }

  @Override
  public List<ReservationDetail> findAll() {
    return jpaRepository.findAll()
        .stream()
        .map(ReservationDetailMapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public List<ReservationDetail> findByReservationId(Long reservationId) {
    return jpaRepository.findByReservation_Id(reservationId)
        .stream()
        .map(ReservationDetailMapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public List<ReservationDetail> findByToolId(Long toolId) {
    return jpaRepository.findByTool_Id(toolId)
        .stream()
        .map(ReservationDetailMapper::toDomain)
        .collect(Collectors.toList());
  }

  /*
   * =========================
   * DELETE
   * =========================
   */
  @Override
  public void delete(ReservationDetail reservationDetail) {
    ReservationDetailEntity entity =
        ReservationDetailMapper.toEntity(reservationDetail);
    jpaRepository.delete(entity);
  }

  @Override
  public void deleteByReservationId(Long reservationId) {
    jpaRepository.deleteByReservation_Id(reservationId);
  }
}
