package com.rentaherramientas.tolly.application.usecase.tool;

import org.springframework.stereotype.Service;
import com.rentaherramientas.tolly.domain.ports.ToolStatusRepository;
import com.rentaherramientas.tolly.application.mapper.ToolStatusMapper;
import com.rentaherramientas.tolly.application.dto.tool.ToolStatusResponse;
import java.util.List;

/**
 * UseCase para obtener todos los estados de herramientas disponibles
 */
@Service
public class GetToolStatusesUseCase {
    private final ToolStatusRepository toolStatusRepository;
    private final ToolStatusMapper toolStatusMapper;
    
    public GetToolStatusesUseCase(ToolStatusRepository toolStatusRepository, ToolStatusMapper toolStatusMapper) {
        this.toolStatusRepository = toolStatusRepository;
        this.toolStatusMapper = toolStatusMapper;
    }
    
    public List<ToolStatusResponse> execute() {
        return toolStatusRepository.findAll()
            .stream()
            .map(toolStatusMapper::toToolStatusResponse)
            .toList();
    }
}
