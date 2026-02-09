package com.rentaherramientas.tolly.domain.ports;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rentaherramientas.tolly.domain.model.Reservation;

public interface ReservationRepository {

  // Crear o actualizar
    Reservation save(Reservation reservation);

    // Leer
    Optional<Reservation> findById(Long id);

    // Borrar
    void delete(Reservation reservation);

    // Listar todas
    List<Reservation> findAll();

    // Listar por cliente
    List<Reservation> findByClientId(Long clientId);

    List<Reservation> findByStartDateRange(LocalDate from, LocalDate to);

    List<Object[]> findFrequentClients(LocalDate from, LocalDate to);

    List<Reservation> findBySupplierIdAndFilters(
        Long supplierId,
        String statusName,
        LocalDate from,
        LocalDate to);

    Page<Reservation> findByClientIdAndFilters(
        Long clientId,
        String statusName,
        LocalDate from,
        LocalDate to,
        Pageable pageable);

    Page<Reservation> findBySupplierIdAndFilters(
        Long supplierId,
        String statusName,
        LocalDate from,
        LocalDate to,
        Pageable pageable);

}
