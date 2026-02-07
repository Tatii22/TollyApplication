package com.rentaherramientas.tolly.domain.ports;

import java.util.Optional;
import java.util.List;
import com.rentaherramientas.tolly.domain.model.ToolStatus;

public interface ToolStatusRepository {
    Optional<ToolStatus> findById(Long id);

    Optional<ToolStatus> findByName(String name);

    ToolStatus save(ToolStatus toolStatus);

    List<ToolStatus> findAll();
}
