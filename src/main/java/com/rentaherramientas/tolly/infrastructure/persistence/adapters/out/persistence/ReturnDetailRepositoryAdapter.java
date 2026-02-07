package com.rentaherramientas.tolly.infrastructure.persistence.adapters.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.rentaherramientas.tolly.application.mapper.ReturnDetailMapper;
import com.rentaherramientas.tolly.domain.model.ReturnDetail;
import com.rentaherramientas.tolly.domain.ports.ReturnDetailRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ReturnDetailEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ReturnDetailJpaRepository;

@Repository
public class ReturnDetailRepositoryAdapter implements ReturnDetailRepository {

    private final ReturnDetailJpaRepository jpaRepository;

    public ReturnDetailRepositoryAdapter(ReturnDetailJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public ReturnDetail save(ReturnDetail returnDetail) {
        ReturnDetailEntity entity = ReturnDetailMapper.toEntity(returnDetail);
        ReturnDetailEntity saved = jpaRepository.save(entity);
        return ReturnDetailMapper.toDomain(saved);
    }

    @Override
    public Optional<ReturnDetail> findById(Long id) {
        return jpaRepository.findById(id)
            .map(ReturnDetailMapper::toDomain);
    }

    @Override
    public List<ReturnDetail> findAll() {
        return jpaRepository.findAll()
            .stream()
            .map(ReturnDetailMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<ReturnDetail> findByReturnId(Long returnId) {
        return jpaRepository.findByReturnValue_Id(returnId)
            .stream()
            .map(ReturnDetailMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<ReturnDetail> findByToolId(Long toolId) {
        return jpaRepository.findByTool_Id(toolId)
            .stream()
            .map(ReturnDetailMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsByReturnIdAndToolId(Long returnId, Long toolId) {
        return jpaRepository.existsByReturnValue_IdAndTool_Id(returnId, toolId);
    }

    @Override
    public void delete(ReturnDetail returnDetail) {
        ReturnDetailEntity entity = ReturnDetailMapper.toEntity(returnDetail);
        jpaRepository.delete(entity);
    }

    @Override
    public void deleteByReturnId(Long returnId) {
        jpaRepository.deleteByReturnValue_Id(returnId);
    }
}
