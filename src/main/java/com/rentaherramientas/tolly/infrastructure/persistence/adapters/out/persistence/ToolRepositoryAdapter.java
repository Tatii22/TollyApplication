package com.rentaherramientas.tolly.infrastructure.persistence.adapters.out.persistence;
import java.util.List;
import java.util.Optional;

import com.rentaherramientas.tolly.domain.model.Tool;
import com.rentaherramientas.tolly.domain.model.enums.ToolStatus;
import com.rentaherramientas.tolly.domain.ports.ToolRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ToolEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ToolJpaRepository;
import com.rentaherramientas.tolly.application.mapper.ToolMapper;

public class ToolRepositoryAdapter implements ToolRepository {
    private final ToolJpaRepository toolJpaRepository;
    public ToolRepositoryAdapter(ToolJpaRepository toolJpaRepository) {
        this.toolJpaRepository = toolJpaRepository;
    }
    @Override
    public List<Tool> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }
    @Override
    public Optional<Tool> findById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }
    @Override
    public Optional<Tool> update(Long id, Tool tool) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
    @Override
    public Tool save(Tool tool) {
    ToolEntity toolEntity = ToolRepositoryAdapter.toEntity(tool);
    ToolEntity savedToolEntity = toolJpaRepository.save(toolEntity);
    return ToolRepositoryAdapter.toDomain(savedToolEntity);
}
    @Override
    public Optional<Tool> deleteById(Long id) {
        var existing = toolJpaRepository.findById(id);
        if (existing.isEmpty()) return Optional.empty();
        toolJpaRepository.deleteById(id);
        return existing.map(ToolRepositoryAdapter::toDomain);
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
