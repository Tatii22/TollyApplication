package com.rentaherramientas.tolly.application.usecase.tool;

import com.rentaherramientas.tolly.application.dto.tool.ToolPublicResponse;
import com.rentaherramientas.tolly.application.dto.tool.ToolResponse;
import com.rentaherramientas.tolly.application.mapper.ToolMapper;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.ports.ToolRepository;
import org.springframework.stereotype.Service;

/**
 * UseCase para obtener una herramienta por ID
 */
@Service
public class GetToolByIdUseCase {
    
    private final ToolRepository toolRepository;
    private final ToolMapper toolMapper;
    
    public GetToolByIdUseCase(ToolRepository toolRepository, ToolMapper toolMapper) {
        this.toolRepository = toolRepository;
        this.toolMapper = toolMapper;
    }
    
    public ToolResponse execute(Long id) {
        return toolRepository.findById(id)
            .map(toolMapper::toToolResponse)
            .orElseThrow(() -> new DomainException("Herramienta con ID " + id + " no encontrada"));
    }

    public ToolPublicResponse executePublic(Long id) {
        return toolRepository.findById(id)
            .map(toolMapper::toToolPublicResponse)
            .orElseThrow(() -> new DomainException("Herramienta con ID " + id + " no encontrada"));
    }
}
