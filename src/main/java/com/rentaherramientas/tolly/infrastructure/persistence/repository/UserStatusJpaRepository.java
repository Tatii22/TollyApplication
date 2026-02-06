package com.rentaherramientas.tolly.infrastructure.persistence.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.UserStatusEntity;

@Repository
public interface UserStatusJpaRepository extends JpaRepository<UserStatusEntity, Long> {

    boolean existsByName(String name);
    Optional<UserStatusEntity> findByName(String name);

}


