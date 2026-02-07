package com.rentaherramientas.tolly.domain.ports;

import java.util.List;
import java.util.Optional;

import com.rentaherramientas.tolly.domain.model.ToolImage;

public interface ToolImageRepository {
    ToolImage save(ToolImage toolImage);

    List<ToolImage> findByToolId(Long toolId);

    Optional<ToolImage> findById(Long id);

    void deleteById(Long id);
}
