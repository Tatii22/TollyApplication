package com.rentaherramientas.tolly.domain.ports;

import java.util.Optional;

import com.rentaherramientas.tolly.domain.model.UserStatus;


public interface UserStatusRepository {
  boolean existsByName(String name);
  Optional<UserStatus> findByName(String name);
  UserStatus save(UserStatus user);
}
