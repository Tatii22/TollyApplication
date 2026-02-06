package com.rentaherramientas.tolly.domain.ports;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


import com.rentaherramientas.tolly.domain.model.ReservationDetail;

public interface ReservationDetailRepository {

  // CREATE / UPDATE
  ReservationDetail save(ReservationDetail reservationDetail);

  // READ
  Optional<ReservationDetail> findById(Long id);

  List<ReservationDetail> findAll();

  // Detalles de una reserva
  List<ReservationDetail> findByReservationId(Long reservationId);

  // Detalles por herramienta
  List<ReservationDetail> findByToolId(Long toolId);

  boolean existsByReservationIdAndToolId(Long reservationId, Long toolId);

  // DELETE
  void delete(ReservationDetail reservationDetail);

  void deleteByReservationId(Long reservationId);
}
