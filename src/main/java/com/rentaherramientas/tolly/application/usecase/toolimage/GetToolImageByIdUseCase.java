package com.rentaherramientas.tolly.application.usecase.toolimage;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.application.dto.tool.ToolImageResponse;
import com.rentaherramientas.tolly.application.mapper.ToolImageMapper;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.ports.ToolImageRepository;

/**
 * UseCase para obtener imagen por ID
 */
@Service
public class GetToolImageByIdUseCase {

    private final ToolImageRepository toolImageRepository;
    private final ToolImageMapper toolImageMapper;

    public GetToolImageByIdUseCase(ToolImageRepository toolImageRepository, ToolImageMapper toolImageMapper) {
        this.toolImageRepository = toolImageRepository;
        this.toolImageMapper = toolImageMapper;
    }

    public ToolImageResponse execute(Long id) {
        return toolImageRepository.findById(id)
            .map(toolImageMapper::toResponse)
            .orElseThrow(() -> new DomainException("Imagen con ID " + id + " no encontrada"));
    }
}
