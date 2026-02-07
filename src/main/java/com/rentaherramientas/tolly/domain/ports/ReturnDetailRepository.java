package com.rentaherramientas.tolly.domain.ports;

import java.util.List;
import java.util.Optional;

import com.rentaherramientas.tolly.domain.model.ReturnDetail;

public interface ReturnDetailRepository {

    ReturnDetail save(ReturnDetail returnDetail);

    Optional<ReturnDetail> findById(Long id);

    List<ReturnDetail> findAll();

    List<ReturnDetail> findByReturnId(Long returnId);

    List<ReturnDetail> findByToolId(Long toolId);

    boolean existsByReturnIdAndToolId(Long returnId, Long toolId);

    void delete(ReturnDetail returnDetail);

    void deleteByReturnId(Long returnId);
}
