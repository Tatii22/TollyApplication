package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.PaymentEntity;

@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {

    /*
    =========================
    CRUD BÁSICO (JpaRepository)
    =========================

    Guardar / Actualizar
    PaymentEntity save(PaymentEntity entity);

    Buscar por ID
    Optional<PaymentEntity> findById(Long id);

    Listar todos
    List<PaymentEntity> findAll();

    Eliminar
    void delete(PaymentEntity entity);

    void deleteById(Long id);
    */

    /*
    =========================
    CONSULTAS PERSONALIZADAS
    =========================

    // Buscar pagos por reserva
    List<PaymentEntity> findByReservationId(Long reservationId);

    // Buscar pagos por estado
    List<PaymentEntity> findByStatus_Name(String name);
    */
}
