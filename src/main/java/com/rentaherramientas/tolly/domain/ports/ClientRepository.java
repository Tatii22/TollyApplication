package com.rentaherramientas.tolly.domain.ports;

import java.util.Optional;

import com.rentaherramientas.tolly.domain.model.Client;
import com.rentaherramientas.tolly.domain.model.User;

public interface ClientRepository {
  void save(Client client);
  Optional<Client> findByUserId(User userId);
  void delete(Client client);
  //Optional<Client> findByDocument(String document);

}
