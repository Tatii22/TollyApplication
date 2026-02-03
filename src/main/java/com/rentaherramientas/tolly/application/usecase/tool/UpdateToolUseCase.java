package com.rentaherramientas.tolly.application.usecase.tool;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.rentaherramientas.tolly.domain.ports.ToolRepository;
import com.rentaherramientas.tolly.domain.model.Tool;
import com.rentaherramientas.tolly.application.mapper.ToolMapper;
import com.rentaherramientas.tolly.application.dto.tool.UpdateToolRequest;
import com.rentaherramientas.tolly.application.dto.tool.ToolResponse;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;

/**
 * UseCase para actualizar una herramienta
 * Solo permite actualizar: name, description, dailyCost y stock
 * El status solo se puede cambiar a través de otros usecases
 */
@Service
public class UpdateToolUseCase {
    private final ToolRepository toolRepository;
    private final ToolMapper toolMapper;
    
    public UpdateToolUseCase(ToolRepository toolRepository, ToolMapper toolMapper) {
        this.toolRepository = toolRepository;
        this.toolMapper = toolMapper;
    }
    
    @Transactional
    public ToolResponse execute(Long id, UpdateToolRequest request) {
        // Verificar que la herramienta existe
        Tool existing = toolRepository.findById(id)
            .orElseThrow(() -> new DomainException("Herramienta con ID " + id + " no encontrada"));
        
        // Actualizar solo los campos permitidos
        if (request.getName() != null && !request.getName().isBlank()) {
            existing.setName(request.getName());
        }
        
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            existing.setDescription(request.getDescription());
        }
        
        if (request.getDailyCost() != null && request.getDailyCost() >= 0) {
            existing.setDailyCost(request.getDailyCost());
        }
        
        if (request.getStock() != null && request.getStock() >= 0) {
            existing.setStock(request.getStock());
        }
        
        Tool saved = toolRepository.save(existing);
        return toolMapper.toToolResponse(saved);
    }
}

