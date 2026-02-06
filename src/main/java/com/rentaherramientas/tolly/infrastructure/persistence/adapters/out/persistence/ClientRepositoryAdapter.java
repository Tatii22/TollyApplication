package com.rentaherramientas.tolly.infrastructure.persistence.adapters.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.rentaherramientas.tolly.application.mapper.ClientMapper;
import com.rentaherramientas.tolly.domain.model.Client;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ClientEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.UserEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.ClientJpaRepository;
import com.rentaherramientas.tolly.infrastructure.persistence.repository.UserJpaRepository;

@Component
public class ClientRepositoryAdapter implements ClientRepository {

  private final ClientJpaRepository clientRepository;
  private final UserJpaRepository userRepository;
  private final ClientMapper mapper;

  public ClientRepositoryAdapter(
      ClientJpaRepository clientRepository,
      UserJpaRepository userRepository,
      ClientMapper mapper
  ) {
    this.clientRepository = clientRepository;
    this.userRepository = userRepository;
    this.mapper = mapper;
  }

  @Override
  public void save(Client client) {

    // 1️⃣ Buscar UserEntity (FK real)
    UserEntity userEntity = userRepository.findById(client.getUserId().getId())
        .orElseThrow(() -> new RuntimeException("User no encontrado"));

    // 2️⃣ Mapear dominio → entidad
    ClientEntity entity = mapper.toEntity(client, userEntity);

    // 3️⃣ Guardar
    clientRepository.save(entity);
  }

  @Override
  public Optional<Client> findById(Long id) {
    return clientRepository.findById(id).map(mapper::toDomain);
  }

  @Override
public Optional<Client> findByUserId(User user) {

  return userRepository.findById(user.getId())
      .flatMap(clientRepository::findByUserId).map(mapper::toDomain);
}


  @Override
  public void delete(Client client) {

    // ✔ forma limpia
    clientRepository.deleteById(client.getId());
  }
}
