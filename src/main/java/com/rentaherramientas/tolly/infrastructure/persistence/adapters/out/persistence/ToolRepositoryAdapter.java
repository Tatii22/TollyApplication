package com.rentaherramientas.tolly.infrastructure.persistence.adapters.out.persistence;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rentaherramientas.tolly.domain.model.Tool;
import com.rentaherramientas.tolly.domain.ports.ToolRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ToolEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ToolJpaRepository;

public class ToolRepositoryAdapter implements ToolRepository {
    private final ToolJpaRepository toolJpaRepository;
    public ToolRepositoryAdapter(ToolJpaRepository toolJpaRepository) {
        this.toolJpaRepository = toolJpaRepository;
    }
    @Override
    public List<Tool> findAll() {
        return toolJpaRepository.findAll().stream()
            .map(ToolRepositoryAdapter::toDomain)
            .collect(Collectors.toList());
    }
    @Override
    public Optional<Tool> findById(Long id) {
        return toolJpaRepository.findById(id).map(ToolRepositoryAdapter::toDomain);
    }
    @Override
    public Optional<Tool> update(Long id, Tool tool) {
        return toolJpaRepository.findById(id)
            .map(existing -> {
                existing.setName(tool.getName());
                ToolEntity updated = toolJpaRepository.save(existing);
                return ToolRepositoryAdapter.toDomain(updated);
            });
    }
    @Override
    public Tool save(Tool tool) {
    ToolEntity toolEntity = ToolRepositoryAdapter.toEntity(tool);
    ToolEntity savedToolEntity = toolJpaRepository.save(toolEntity);
    return ToolRepositoryAdapter.toDomain(savedToolEntity);
}
    @Override
    public void deleteById(Long id) {
        toolJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Tool> findByName(String name) {
        return toolJpaRepository.findByName(name).map(ToolRepositoryAdapter::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return toolJpaRepository.existsByName(name);
    }

    public static Tool toDomain(ToolEntity toolEntity) {
        Tool tool = new Tool();
        tool.setId(toolEntity.getId());
        tool.setSupplierId(toolEntity.getSupplierId());
        tool.setCategoryId(toolEntity.getCategoryId());
        tool.setName(toolEntity.getName());
        tool.setDescription(toolEntity.getDescription());
        tool.setDailyCost(toolEntity.getDailyCost());
        tool.setStatus(toolEntity.getStatus()); 
        return tool;
    }

    // Convertir Tool (dominio) a ToolEntity
    public static ToolEntity toEntity(Tool tool) {
        ToolEntity toolEntity = new ToolEntity();
        toolEntity.setId(tool.getId());
        toolEntity.setSupplierId(tool.getSupplierId());
        toolEntity.setCategoryId(tool.getCategoryId());
        toolEntity.setName(tool.getName());
        toolEntity.setDescription(tool.getDescription());
        toolEntity.setDailyCost(tool.getDailyCost());
        toolEntity.setStatus(tool.getStatus());
        return toolEntity;
    }
}
