package com.rentaherramientas.tolly.application.usecase.tool;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.rentaherramientas.tolly.domain.ports.ToolStatusRepository;
import com.rentaherramientas.tolly.domain.model.ToolStatus;
import com.rentaherramientas.tolly.application.mapper.ToolStatusMapper;
import com.rentaherramientas.tolly.application.dto.tool.CreateToolStatusRequest;
import com.rentaherramientas.tolly.application.dto.tool.ToolStatusResponse;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;

/**
 * UseCase para crear un nuevo estado de herramienta
 * Solo los ADMIN pueden crear estados globales del sistema
 */
@Service
public class CreateToolStatusUseCase {
    private final ToolStatusRepository toolStatusRepository;
    private final ToolStatusMapper toolStatusMapper;
    
    public CreateToolStatusUseCase(ToolStatusRepository toolStatusRepository, ToolStatusMapper toolStatusMapper) {
        this.toolStatusRepository = toolStatusRepository;
        this.toolStatusMapper = toolStatusMapper;
    }
    
    @Transactional
    public ToolStatusResponse execute(CreateToolStatusRequest request) {
        // Validar que no exista otro estado con el mismo nombre
        // Aquí podrías agregar un método findByName en ToolStatusRepository si lo necesitas
        
        // Crear el estado
        ToolStatus toolStatus = toolStatusMapper.toToolStatus(request);
        ToolStatus saved = toolStatusRepository.save(toolStatus);
        
        return toolStatusMapper.toToolStatusResponse(saved);
    }
}
