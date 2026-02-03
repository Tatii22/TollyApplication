package com.rentaherramientas.tolly.application.usecase.tool;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.rentaherramientas.tolly.domain.ports.ToolRepository;
import com.rentaherramientas.tolly.domain.model.Tool;
import com.rentaherramientas.tolly.application.mapper.ToolMapper;
import com.rentaherramientas.tolly.application.dto.tool.CreateToolRequest;
import com.rentaherramientas.tolly.application.dto.tool.ToolResponse;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;


/**
 * UseCase para crear una nueva herramienta
 * - Solo SUPPLIERS pueden crear herramientas
 * - El estado inicial es AVAIBLE (disponible)
 * - La herramienta se asocia a un supplier y una categoría
 */
@Service
public class CreateToolUseCase {
    private final ToolRepository toolRepository;
    private final ToolMapper toolMapper;
    
    public CreateToolUseCase(ToolRepository toolRepository, ToolMapper toolMapper) {
        this.toolRepository = toolRepository;
        this.toolMapper = toolMapper;
    }
    
    @Transactional
    public ToolResponse execute(CreateToolRequest request) {
        // Verificar si la herramienta ya existe con el mismo nombre
        if (toolRepository.existsByName(request.getName())) {
            throw new DomainException("Ya existe una herramienta con el nombre: " + request.getName());
        }
        
        // Crear la herramienta (ToolMapper asigna status = AVAIBLE automáticamente)
        Tool tool = toolMapper.toTool(request);
        Tool saved = toolRepository.save(tool);
        
        return toolMapper.toToolResponse(saved);
    }
}
