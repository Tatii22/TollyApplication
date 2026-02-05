package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReturnStatusEntity;

@Repository
public interface ReturnStatusJpaRepository extends JpaRepository<ReturnStatusEntity, Long> {

    /*
    =========================
    CRUD BÁSICO (JpaRepository)
    =========================

    Guardar / Actualizar
    ReturnStatusEntity save(ReturnStatusEntity entity);

    Buscar por ID
    Optional<ReturnStatusEntity> findById(Long id);

    Listar todos
    List<ReturnStatusEntity> findAll();

    Eliminar
    void delete(ReturnStatusEntity entity);

    void deleteById(Long id);
    */

    /*
    =========================
    CONSULTAS PERSONALIZADAS
    =========================


    // Buscar estado por nombre
    Optional<ReturnStatusEntity> findByName(String name);

    */
}
