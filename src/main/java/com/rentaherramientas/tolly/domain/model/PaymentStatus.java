package com.rentaherramientas.tolly.domain.model;

import java.util.Objects;

public class PaymentStatus {

    private Long id;
    private String name;

    // Constructor vacío
    public PaymentStatus() {
    }

    // Constructor para CREATE
    private PaymentStatus(String name) {
        this.name = normalize(name);
        validate();
    }

    // Constructor para RECONSTRUCT
    private PaymentStatus(Long id, String name) {
        this.id = id;
        this.name = normalize(name);
        validate();
    }

    // CREATE (nuevo en el sistema)
    public static PaymentStatus create(String name) {
        return new PaymentStatus(name);
    }

    // RECONSTRUCT (viene desde BD)
    public static PaymentStatus reconstruct(Long id, String name) {
        if (id == null) {
            throw new IllegalArgumentException("Id is required for reconstruction");
        }
        return new PaymentStatus(id, name);
    }

    // Normalización a MAYÚSCULAS
    private String normalize(String name) {
        return name == null ? null : name.trim().toUpperCase();
    }

    // Validaciones de dominio
    private void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Payment status name is required");
        }
    }

    // Indica si ya existe en persistencia
    public boolean exists() {
        return this.id != null;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = normalize(name);
        validate();
    }

    // equals y hashCode por ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentStatus)) return false;
        PaymentStatus that = (PaymentStatus) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
