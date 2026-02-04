package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rentaherramientas.tolly.infrastructure.persistence.entity.ClientEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientJpaRepository extends JpaRepository<ClientEntity, UUID> {

  Optional<ClientEntity> findByUserId(UserEntity userId);


}
