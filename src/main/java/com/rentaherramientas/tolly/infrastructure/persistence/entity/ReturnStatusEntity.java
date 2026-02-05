package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "return_status",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
    }
)
public class ReturnStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    // Constructor vacío requerido por JPA
    public ReturnStatusEntity() {
    }

    // Constructor sin ID
    public ReturnStatusEntity(String name) {
        this.name = name;
    }

    // Constructor completo
    public ReturnStatusEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters y Setters

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
}
