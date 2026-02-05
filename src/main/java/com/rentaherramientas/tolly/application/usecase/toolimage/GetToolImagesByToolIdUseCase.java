package com.rentaherramientas.tolly.application.usecase.toolimage;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.application.dto.tool.ToolImageResponse;
import com.rentaherramientas.tolly.application.mapper.ToolImageMapper;
import com.rentaherramientas.tolly.domain.ports.ToolImageRepository;

/**
 * UseCase para listar imágenes por herramienta
 */
@Service
public class GetToolImagesByToolIdUseCase {

    private final ToolImageRepository toolImageRepository;
    private final ToolImageMapper toolImageMapper;

    public GetToolImagesByToolIdUseCase(ToolImageRepository toolImageRepository, ToolImageMapper toolImageMapper) {
        this.toolImageRepository = toolImageRepository;
        this.toolImageMapper = toolImageMapper;
    }

    public List<ToolImageResponse> execute(Long toolId) {
        return toolImageRepository.findByToolId(toolId).stream()
            .map(toolImageMapper::toResponse)
            .collect(Collectors.toList());
    }
}
