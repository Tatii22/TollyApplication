package com.rentaherramientas.tolly.application.usecase.tool;

import com.rentaherramientas.tolly.application.dto.tool.ToolPublicResponse;
import com.rentaherramientas.tolly.application.dto.tool.ToolResponse;
import com.rentaherramientas.tolly.application.mapper.ToolMapper;
import com.rentaherramientas.tolly.domain.ports.ToolRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * UseCase para obtener todas las herramientas
 */
@Service
public class GetToolsUseCase {
    private static final String AVAILABLE_STATUS_NAME = "AVAILABLE";
    private static final int MIN_AVAILABLE_QUANTITY = 0;

    private final ToolRepository toolRepository;
    private final ToolMapper toolMapper;
    
    public GetToolsUseCase(ToolRepository toolRepository, ToolMapper toolMapper) {
        this.toolRepository = toolRepository;
        this.toolMapper = toolMapper;
    }
    
    public List<ToolResponse> execute() {
        return execute(false);
    }

    public List<ToolResponse> execute(boolean availableOnly) {
        return execute(availableOnly, null);
    }

    public List<ToolResponse> execute(boolean availableOnly, Long categoryId) {
        return execute(availableOnly, categoryId, null, null);
    }

    public List<ToolResponse> execute(boolean availableOnly, Long categoryId, Double minPrice, Double maxPrice) {
        String statusName = availableOnly ? AVAILABLE_STATUS_NAME : null;
        Integer minAvailable = availableOnly ? MIN_AVAILABLE_QUANTITY : null;

        return toolRepository
            .findByFilters(categoryId, statusName, minAvailable, minPrice, maxPrice)
            .stream()
            .map(toolMapper::toToolResponse)
            .toList();
    }

    public List<ToolPublicResponse> executePublic(boolean availableOnly, Long categoryId) {
        return executePublic(availableOnly, categoryId, null, null);
    }

    public List<ToolPublicResponse> executePublic(boolean availableOnly, Long categoryId, Double minPrice, Double maxPrice) {
        String statusName = availableOnly ? AVAILABLE_STATUS_NAME : null;
        Integer minAvailable = availableOnly ? MIN_AVAILABLE_QUANTITY : null;

        return toolRepository
            .findByFilters(categoryId, statusName, minAvailable, minPrice, maxPrice)
            .stream()
            .map(toolMapper::toToolPublicResponse)
            .toList();
    }
}
