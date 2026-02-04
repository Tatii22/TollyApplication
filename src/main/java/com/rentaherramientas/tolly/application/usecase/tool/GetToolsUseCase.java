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
    
    private final ToolRepository toolRepository;
    private final ToolMapper toolMapper;
    
    public GetToolsUseCase(ToolRepository toolRepository, ToolMapper toolMapper) {
        this.toolRepository = toolRepository;
        this.toolMapper = toolMapper;
    }
    
    public List<ToolResponse> execute() {
        return toolRepository.findAll()
            .stream()
            .map(toolMapper::toToolResponse)
            .toList();
    }
}
