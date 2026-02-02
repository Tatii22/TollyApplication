package com.rentaherramientas.tolly.application.mapper;

import com.rentaherramientas.tolly.domain.model.Client;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ClientEntity;

public class ClientMapper {

  private ClientMapper(){
  }

  public static ClientEntity toEntity(Client client) {
        ClientEntity entity = new ClientEntity();
        entity.setId(client.getId());
        entity.setUserId(client.getUserId());
        entity.setAddress(client.getAddress());
        return entity;
    }

    public static Client toDomain(ClientEntity entity) {
        return Client.restore(
            entity.getId(),
            entity.getUserId(),
            entity.getAddress()
        );
    }
}
