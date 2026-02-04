package com.rentaherramientas.tolly.infrastructure.persistence.adapters.out.persistence;



import java.util.Optional;

import org.springframework.stereotype.Component;


import com.rentaherramientas.tolly.domain.model.UserStatus;
import com.rentaherramientas.tolly.domain.ports.UserStatusRepository;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.UserStatusEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.UserStatusJpaRepository;

@Component
public class UserStatusRepositoryAdapter implements UserStatusRepository {

  private final UserStatusJpaRepository jpaRepository;

  public UserStatusRepositoryAdapter(UserStatusJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public boolean existsByName(String name) {
    return jpaRepository.existsByName(name);
  }

  @Override
  public UserStatus save(UserStatus userStatus) {
    UserStatusEntity entity = toEntity(userStatus);
    UserStatusEntity saved = jpaRepository.save(entity);
    return toDomain(saved);
  }

  public UserStatus toDomain(UserStatusEntity entity) {
    return UserStatus.reconstruct(
        entity.getId(),
        entity.getName()
    );
  }
  public UserStatusEntity toEntity(UserStatus status) {
    UserStatusEntity entity = new UserStatusEntity();
    entity.setId(status.getId());
    entity.setName(status.getStatusName());
    return entity;
  }

  @Override
  public Optional<UserStatus> findByName(String name) {
    return jpaRepository.findByName(name)
        .map(this::toDomain);
  }

}
