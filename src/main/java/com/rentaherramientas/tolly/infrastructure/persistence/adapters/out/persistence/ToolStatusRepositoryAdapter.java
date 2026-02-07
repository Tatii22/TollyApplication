package com.rentaherramientas.tolly.infrastructure.persistence.adapters.out.persistence;

import java.util.Optional;
import java.util.List;
import com.rentaherramientas.tolly.domain.model.ToolStatus;
import com.rentaherramientas.tolly.domain.ports.ToolStatusRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ToolStatusEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ToolStatusJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class ToolStatusRepositoryAdapter implements ToolStatusRepository {

    private final ToolStatusJpaRepository jpaRepository;

    public ToolStatusRepositoryAdapter(ToolStatusJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<ToolStatus> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<ToolStatus> findByName(String name) {
        return jpaRepository.findByName(name).map(this::toDomain);
    }

    @Override
    public ToolStatus save(ToolStatus toolStatus) {
        ToolStatusEntity entity = new ToolStatusEntity();
        entity.setName(toolStatus.getName());
        ToolStatusEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<ToolStatus> findAll() {
        return jpaRepository.findAll()
            .stream()
            .map(this::toDomain)
            .toList();
    }

    private ToolStatus toDomain(ToolStatusEntity entity) {
        return new ToolStatus(entity.getId(), entity.getName());
    }
}
