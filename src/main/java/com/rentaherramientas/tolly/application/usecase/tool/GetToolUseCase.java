package com.rentaherramientas.tolly.application.usecase.tool;

import com.rentaherramientas.tolly.application.dto.tool.ToolResponse;
import com.rentaherramientas.tolly.application.mapper.ToolMapper;
import com.rentaherramientas.tolly.domain.ports.ToolRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UseCase para listar todas las herramientas
 */
@Service
public class GetToolUseCase {
    
    private final ToolRepository toolRepository;
    private final ToolMapper toolMapper;
    
    public GetToolUseCase(ToolRepository toolRepository, ToolMapper toolMapper) {
        this.toolRepository = toolRepository;
        this.toolMapper = toolMapper;
    }
    
    public List<ToolResponse> execute() {
        return toolRepository.findAll().stream()
            .map(toolMapper::toToolResponse)
            .collect(Collectors.toList());
    }
}
