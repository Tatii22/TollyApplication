package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.PaymentStatusEntity;

@Repository
public interface PaymentStatusJpaRepository extends JpaRepository<PaymentStatusEntity, Long> {

    /*
    =========================
    CRUD BÁSICO (JpaRepository)
    =========================

    Guardar / Actualizar
    PaymentStatusEntity save(PaymentStatusEntity entity);

    Buscar por ID
    Optional<PaymentStatusEntity> findById(Long id);

    Listar todos
    List<PaymentStatusEntity> findAll();

    Eliminar
    void delete(PaymentStatusEntity entity);

    void deleteById(Long id);
    */

    /*
    =========================
    CONSULTAS PERSONALIZADAS
    =========================

    // Buscar estado por nombre
    Optional<PaymentStatusEntity> findByName(String name);
    */
}
