package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReturnEntity;

@Repository
public interface ReturnJpaRepository extends JpaRepository<ReturnEntity, Long> {

    /*
    =========================
    CRUD BÁSICO (JpaRepository)
    =========================

    Guardar / Actualizar
    ReturnEntity save(ReturnEntity entity);

    Buscar por ID
    Optional<ReturnEntity> findById(Long id);

    Listar todos
    List<ReturnEntity> findAll();

    Eliminar
    void delete(ReturnEntity entity);

    void deleteById(Long id);
    */

    /*
    =========================
    CONSULTAS PERSONALIZADAS
    =========================


    // Listar devoluciones por cliente
    List<ReturnEntity> findByClientId(UUID clientId);

    // Listar devoluciones por reserva
    List<ReturnEntity> findByReservationId(Long reservationId);
     */
}
