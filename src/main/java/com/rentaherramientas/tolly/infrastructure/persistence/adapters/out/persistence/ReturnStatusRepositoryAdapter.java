package com.rentaherramientas.tolly.infrastructure.persistence.adapters.out.persistence;

import java.util.Optional;
import java.util.List;
import com.rentaherramientas.tolly.domain.model.ReturnStatus;
import com.rentaherramientas.tolly.domain.ports.ReturnStatusRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReturnStatusEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ReturnStatusJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class ReturnStatusRepositoryAdapter implements ReturnStatusRepository {

    private final ReturnStatusJpaRepository jpaRepository;

    public ReturnStatusRepositoryAdapter(ReturnStatusJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<ReturnStatus> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public ReturnStatus save(ReturnStatus returnStatus) {
        ReturnStatusEntity entity = new ReturnStatusEntity();
        entity.setName(returnStatus.getName());
        ReturnStatusEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<ReturnStatus> findAll() {
        return jpaRepository.findAll()
            .stream()
            .map(this::toDomain)
            .toList();
    }

    private ReturnStatus toDomain(ReturnStatusEntity entity) {
        return new ReturnStatus(entity.getId(), entity.getName());
    }
}
