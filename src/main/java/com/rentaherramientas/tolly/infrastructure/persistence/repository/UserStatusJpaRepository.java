package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.UserStatusEntity;

@Repository
public interface UserStatusJpaRepository extends JpaRepository<UserStatusEntity, UUID> {

    boolean existsByName(String name);
    Optional<UserStatusEntity> findByName(String name);

}


