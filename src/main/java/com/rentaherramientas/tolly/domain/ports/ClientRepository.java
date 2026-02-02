package com.rentaherramientas.tolly.domain.ports;

import java.util.Optional;
import java.util.UUID;

import com.rentaherramientas.tolly.domain.model.Client;

public interface ClientRepository {
  void save(Client client);
  Optional<Client> findByUserId(UUID userId);
  void delete(Client client);

}
