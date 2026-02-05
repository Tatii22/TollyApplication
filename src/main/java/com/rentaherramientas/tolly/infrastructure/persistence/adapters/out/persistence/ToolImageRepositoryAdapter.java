package com.rentaherramientas.tolly.infrastructure.persistence.adapters.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.rentaherramientas.tolly.domain.model.ToolImage;
import com.rentaherramientas.tolly.domain.ports.ToolImageRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ToolEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ToolImageEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ToolImageJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ToolJpaRepository;

@Component
public class ToolImageRepositoryAdapter implements ToolImageRepository {

    private final ToolImageJpaRepository toolImageJpaRepository;
    private final ToolJpaRepository toolJpaRepository;

    public ToolImageRepositoryAdapter(ToolImageJpaRepository toolImageJpaRepository, ToolJpaRepository toolJpaRepository) {
        this.toolImageJpaRepository = toolImageJpaRepository;
        this.toolJpaRepository = toolJpaRepository;
    }

    @Override
    public ToolImage save(ToolImage toolImage) {
        ToolEntity toolEntity = toolJpaRepository.findById(toolImage.getToolId())
            .orElseThrow(() -> new RuntimeException("Tool no encontrado"));
        ToolImageEntity entity = toEntity(toolImage, toolEntity);
        ToolImageEntity saved = toolImageJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<ToolImage> findByToolId(Long toolId) {
        ToolEntity toolEntity = toolJpaRepository.findById(toolId)
            .orElseThrow(() -> new RuntimeException("Tool no encontrado"));
        return toolImageJpaRepository.findByTool(toolEntity).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<ToolImage> findById(Long id) {
        return toolImageJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        toolImageJpaRepository.deleteById(id);
    }

    private ToolImageEntity toEntity(ToolImage toolImage, ToolEntity toolEntity) {
        ToolImageEntity entity = new ToolImageEntity();
        entity.setId(toolImage.getId());
        entity.setTool(toolEntity);
        entity.setimage_url(toolImage.getimage_url());
        return entity;
    }

    private ToolImage toDomain(ToolImageEntity entity) {
        Long toolId = entity.getTool() != null ? entity.getTool().getId() : null;
        return new ToolImage(entity.getId(), toolId, entity.getimage_url());
    }
}
