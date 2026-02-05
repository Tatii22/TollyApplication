package com.rentaherramientas.tolly.application.usecase.tool;

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
        List<ToolResponse> toolResponses = availableOnly
            ? toolRepository
                .findByStatusName(
                    AVAILABLE_STATUS_NAME,
                    MIN_AVAILABLE_QUANTITY)
                .stream()
                .map(toolMapper::toToolResponse)
                .toList()
            : toolRepository.findAll()
                .stream()
                .map(toolMapper::toToolResponse)
                .toList();

        return toolResponses;
    }
}
