package com.rentaherramientas.tolly.domain.ports;
import java.util.Optional;
import java.util.List;

import com.rentaherramientas.tolly.domain.model.Tool;

public interface ToolRepository {
    Tool save(Tool tool);
    Optional<Tool> findById(Long id);
    Optional<Tool> findByName(String name);
    List<Tool> findAll();
    List<Tool> findByStatusName(String statusName, Integer minAvailableQuantity);
    boolean existsByName(String name);
    void deleteById(Long id);
    Optional<Tool> update(Long id, Tool tool);
}
