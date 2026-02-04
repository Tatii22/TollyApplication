package com.rentaherramientas.tolly.infrastructure.persistence.adapters.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.rentaherramientas.tolly.application.mapper.SupplierMapper;
import com.rentaherramientas.tolly.domain.model.Supplier;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.SupplierRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.SupplierEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.UserEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.SupplierJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.UserJpaRepository;

@Component
public class SupplierRepositoryAdapter implements SupplierRepository {

  private final SupplierJpaRepository supplierRepository;
  private final UserJpaRepository userRepository;
  private final SupplierMapper mapper;

  public SupplierRepositoryAdapter(
      SupplierJpaRepository supplierRepository,
      UserJpaRepository userRepository,
      SupplierMapper mapper
  ) {
    this.supplierRepository = supplierRepository;
    this.userRepository = userRepository;
    this.mapper = mapper;
  }

  @Override
  public void save(Supplier supplier) {

    // 1️⃣ Buscar UserEntity
    UserEntity userEntity = userRepository.findById(supplier.getUserId().getId())
        .orElseThrow(() -> new RuntimeException("User no encontrado"));

    // 2️⃣ Mapear dominio → entity
    SupplierEntity entity = mapper.toEntity(supplier, userEntity);

    // 3️⃣ Guardar
    supplierRepository.save(entity);
  }

  @Override
  public Optional<Supplier> findByUserId(User user) {

    return userRepository.findById(user.getId()).flatMap(supplierRepository::findByUserId).map(mapper::toDomain);
  }

  @Override
  public Optional<Supplier> findById(UUID id) {
    return repository.findById(id).map(SupplierMapper::toDomain);
  }

  @Override
  public void delete(Supplier supplier) {

    supplierRepository.deleteById(supplier.getId());
  }
}

