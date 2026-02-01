package com.rentaherramientas.tolly.domain.ports;

import com.rentaherramientas.tolly.domain.model.Client;

public interface ClientRepository {
  void save(Client client);
}
