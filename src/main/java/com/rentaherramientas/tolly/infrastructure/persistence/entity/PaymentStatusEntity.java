package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "payment_status",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
    }
)
public class PaymentStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    // Constructor vacío requerido por JPA
    public PaymentStatusEntity() {
    }

    // Constructor sin ID
    public PaymentStatusEntity(String name) {
        this.name = name;
    }

    // Constructor completo
    public PaymentStatusEntity(Long id, String name) {
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
