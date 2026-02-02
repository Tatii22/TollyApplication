package com.rentaherramientas.tolly.domain.model;

import java.util.UUID;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;

public class Client {

    private UUID id;
    private UUID userId;
    private String address;

    // Constructor privado
    private Client() {}

    private Client(UUID id, UUID userId, String address) {
        this.id = id;
        this.userId = userId;
        this.address = address;
    }

    public static Client create(UUID userId, String address) {

        if (userId == null) {
            throw new DomainException("userId es obligatorio");
        }

        if (address == null || address.isBlank()) {
            throw new DomainException("La dirección es obligatoria para CLIENT");
        }

        Client client = new Client();
        client.id = UUID.randomUUID();
        client.userId = userId;
        client.address = address.trim();

        return client;
    }

    // Métodos de negocio
    public void changeAddress(String newAddress) {
        if (newAddress == null || newAddress.isBlank()) {
            throw new DomainException("La dirección no puede estar vacía");
        }
        this.address = newAddress.trim();
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getAddress() {
        return address;
    }

    public static Client restore(UUID id, UUID userId, String address) {
        return new Client(id, userId, address);
    }
}
