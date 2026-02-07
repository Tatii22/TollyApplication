package com.rentaherramientas.tolly.application.usecase.toolimage;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.application.dto.tool.CreateToolImageRequest;
import com.rentaherramientas.tolly.application.dto.tool.ToolImageResponse;
import com.rentaherramientas.tolly.application.mapper.ToolImageMapper;
import com.rentaherramientas.tolly.domain.model.ToolImage;
import com.rentaherramientas.tolly.domain.ports.ToolImageRepository;

/**
 * UseCase para crear una imagen de herramienta
 */
@Service
public class CreateToolImageUseCase {

    private final ToolImageRepository toolImageRepository;
    private final ToolImageMapper toolImageMapper;

    public CreateToolImageUseCase(ToolImageRepository toolImageRepository, ToolImageMapper toolImageMapper) {
        this.toolImageRepository = toolImageRepository;
        this.toolImageMapper = toolImageMapper;
    }

    @Transactional
    public ToolImageResponse execute(CreateToolImageRequest request) {
        ToolImage toolImage = toolImageMapper.toDomain(request);
        ToolImage saved = toolImageRepository.save(toolImage);
        return toolImageMapper.toResponse(saved);
    }
}
