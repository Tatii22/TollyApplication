package com.rentaherramientas.tolly.infrastructure.persistence.adapters.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.rentaherramientas.tolly.application.mapper.ClientMapper;
import com.rentaherramientas.tolly.domain.model.Client;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ClientEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ClientJpaRepository;

@Component
public class ClientRepositoryAdapter implements ClientRepository {

  private final ClientJpaRepository repository;

  public ClientRepositoryAdapter(ClientJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public void save(Client client) {
    ClientEntity entity = ClientMapper.toEntity(client);
    repository.save(entity);
  }

  @Override
  public Optional<Client> findByUserId(UUID userId) {
    return repository
        .findByUserId(userId)
        .map(ClientMapper::toDomain);
  }

  @Override
  public void delete(Client client) {
    ClientEntity entity = ClientMapper.toEntity(client);
    repository.delete(entity);
  }

}
