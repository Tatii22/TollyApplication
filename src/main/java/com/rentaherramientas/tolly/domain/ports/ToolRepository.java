package com.rentaherramientas.tolly.domain.ports;
import java.util.Optional;
import java.util.List;

import com.rentaherramientas.tolly.domain.model.Tool;

public interface ToolRepository {
    List<Tool> findAll();

    Optional<Tool> findById(Long id);

    Optional<Tool> update(Long id, Tool tool);

    Tool save(Tool tool);
    
    Optional<Tool> deleteById(Long id);

}
