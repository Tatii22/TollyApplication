package com.rentaherramientas.tolly.infrastructure.persistence.adapters.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.rentaherramientas.tolly.application.mapper.SupplierMapper;
import com.rentaherramientas.tolly.domain.model.Supplier;
import com.rentaherramientas.tolly.domain.ports.SupplierRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.SupplierEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.SupplierJpaRepository;

@Component
public class SupplierRepositoryAdapter implements SupplierRepository{

  private final SupplierJpaRepository repository;

  public SupplierRepositoryAdapter(SupplierJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public void save(Supplier supplier) {
    SupplierEntity entity = SupplierMapper.toEntity(supplier);
    repository.save(entity);
  }

  @Override
  public Optional<Supplier> findByUserId(UUID userId) {
    return repository.findByUserId(userId).map(SupplierMapper::toDomain);
  }

  @Override
  public void delete(Supplier supplier) {
    SupplierEntity entity = SupplierMapper.toEntity(supplier);
    repository.delete(entity);
  }

}
