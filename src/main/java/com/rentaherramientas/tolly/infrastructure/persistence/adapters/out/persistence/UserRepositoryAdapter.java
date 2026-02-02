package com.rentaherramientas.tolly.infrastructure.persistence.adapters.out.persistence;

import com.rentaherramientas.tolly.domain.model.Client;
import com.rentaherramientas.tolly.domain.model.Role;
import com.rentaherramientas.tolly.domain.model.Supplier;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.UserRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ClientEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.RoleEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.SupplierEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.UserEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ClientJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.SupplierJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adaptador que conecta el puerto UserRepository con la implementación JPA
 * Convierte entre entidades de dominio y entidades JPA
 */
@Component
public class UserRepositoryAdapter implements UserRepository {

  private final UserJpaRepository jpaRepository;
  private final ClientJpaRepository clientJpaRepository;
  private final SupplierJpaRepository supplierJpaRepository;

  public UserRepositoryAdapter(UserJpaRepository jpaRepository,
      ClientJpaRepository clientJpaRepository,
      SupplierJpaRepository supplierJpaRepository) {
    this.jpaRepository = jpaRepository;
    this.clientJpaRepository = clientJpaRepository;
    this.supplierJpaRepository = supplierJpaRepository;
  }

  @Override
  public User save(User user) {
    UserEntity entity = toEntity(user);
    UserEntity saved = jpaRepository.save(entity);
    return toDomain(saved);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return jpaRepository.findByEmail(email)
        .map(this::toDomain);
  }

  @Override
  public Optional<User> findById(UUID id) {
    return jpaRepository.findById(id)
        .map(this::toDomain);
  }

  @Override
  public boolean existsByEmail(String email) {
    return jpaRepository.existsByEmail(email);
  }

  @Override
  public void delete(User user) {
    UserEntity entity = toEntity(user);
    jpaRepository.delete(entity);
  }

  private UserEntity toEntity(User user) {
    Set<RoleEntity> roleEntities = user.getRoles().stream()
        .map(this::roleToEntity)
        .collect(Collectors.toSet());

    return new UserEntity(
        user.getId(),
        user.getEmail(),
        user.getPassword(),
        roleEntities,
        user.getStatus());
  }

  private User toDomain(UserEntity entity) {
    Set<Role> roles = entity.getRoles().stream()
        .map(this::roleToDomain)
        .collect(Collectors.toSet());

    User user = User.reconstruct(
        entity.getId(),
        entity.getEmail(),
        entity.getPassword(),
        roles,
        entity.getStatus());

    // Cargar datos de Cliente si existen
    Optional<ClientEntity> clientEntity = clientJpaRepository.findByUserId(entity.getId());
    if (clientEntity.isPresent()) {
      ClientEntity client = clientEntity.get();
      user.setClient(Client.restore(client.getId(), client.getUserId(), client.getAddress()));
    }

    // Cargar datos de Proveedor si existen
    Optional<SupplierEntity> supplierEntity = supplierJpaRepository.findByUserId(entity.getId());
    if (supplierEntity.isPresent()) {
      SupplierEntity supplier = supplierEntity.get();
      user.setSupplier(
          new Supplier(supplier.getId(), supplier.getUserId(), supplier.getPhone(), supplier.getCompany()));
    }

    return user;
  }

  private RoleEntity roleToEntity(Role role) {
    return new RoleEntity(
        role.getId(),
        role.getName(),
        role.getAuthority());
  }

  private Role roleToDomain(RoleEntity entity) {
    return Role.reconstruct(
        entity.getId(),
        entity.getName(),
        entity.getAuthority());
  }

  @Override
  public List<User> findAll() {
    return jpaRepository.findAll().stream()
        .map(this::toDomain)
        .collect(Collectors.toList());
  }
}
