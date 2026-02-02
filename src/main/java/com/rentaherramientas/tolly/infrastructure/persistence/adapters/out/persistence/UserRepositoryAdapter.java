package com.rentaherramientas.tolly.infrastructure.persistence.adapters.out.persistence;

import com.rentaherramientas.tolly.domain.model.Role;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.UserRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.RoleEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.UserEntity;
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

    public UserRepositoryAdapter(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
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
            user.getStatus()
        );
    }

    private User toDomain(UserEntity entity) {
        Set<Role> roles = entity.getRoles().stream()
            .map(this::roleToDomain)
            .collect(Collectors.toSet());

        return User.reconstruct(
            entity.getId(),
            entity.getEmail(),
            entity.getPassword(),
            roles,
            entity.getStatus()
        );
    }

    private RoleEntity roleToEntity(Role role) {
        return new RoleEntity(
            role.getId(),
            role.getName(),
            role.getAuthority()
        );
    }

    private Role roleToDomain(RoleEntity entity) {
        return Role.reconstruct(
            entity.getId(),
            entity.getName(),
            entity.getAuthority()
        );
    }

    @Override
    public List<User> findAll() {
      return jpaRepository.findAll().stream()
        .map(this::toDomain)
        .collect(Collectors.toList());
    }
}
