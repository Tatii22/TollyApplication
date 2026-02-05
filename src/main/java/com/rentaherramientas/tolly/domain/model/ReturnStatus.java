package com.rentaherramientas.tolly.domain.model;

import java.util.Objects;

public class ReturnStatus {

    private Long id;
    private String name;

    // ✅ Constructor vacío
    public ReturnStatus() {
    }

    // ✅ Constructor sin ID
    public ReturnStatus(String name) {
        this.name = name;
        validate();
    }

    // ✅ Constructor completo
    public ReturnStatus(Long id, String name) {
        this.id = id;
        this.name = name;
        validate();
    }

    // 🔹 Método de creación
    public static ReturnStatus create(String name) {
        return new ReturnStatus(name);
    }

    // 🔹 Validación
    private void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Return status name is indicated");
        }
    }

    // 🔹 Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // 🔹 equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReturnStatus)) return false;
        ReturnStatus that = (ReturnStatus) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

