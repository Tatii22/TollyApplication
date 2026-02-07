package com.rentaherramientas.tolly.application.mapper;

import org.springframework.stereotype.Component;

import com.rentaherramientas.tolly.domain.model.Client;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ClientEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.UserEntity;

@Component
public class ClientMapper {

  private ClientMapper() {
  }

  public ClientEntity toEntity(Client client, UserEntity userEntity) {
    ClientEntity entity = new ClientEntity();
    entity.setId(client.getId());
    entity.setUserId(userEntity);
    entity.setAddress(client.getAddress());
    entity.setPhoneNumber(client.getPhone());
    entity.setFirstName(client.getFirstName());
    entity.setLastName(client.getLastName());
    entity.setDocumentId(client.getDocument());
    return entity;
  }

  public Client toDomain(ClientEntity entity) {
    return Client.restore(
        entity.getId(),
        User.restore(entity.getUserId().getId()),
        entity.getAddress(),
        entity.getPhoneNumber(),
        entity.getFirstName(),
        entity.getLastName(),
        entity.getDocumentId());
  }
}
