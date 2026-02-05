package com.rentaherramientas.tolly.domain.ports;

import java.util.List;
import java.util.Optional;

import com.rentaherramientas.tolly.domain.model.ReturnStatus;

public interface ReturnStatusRepository {

    // CREATE / UPDATE
    ReturnStatus save(ReturnStatus status);

    // READ
    Optional<ReturnStatus> findById(Long id);

    Optional<ReturnStatus> findByName(String name);

    List<ReturnStatus> findAll();

    // DELETE
    void delete(ReturnStatus status);

    void deleteById(Long id);
}

