package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "payment_status")
public class PaymentStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_payment_status")
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 30)
    private String name;

    public PaymentStatusEntity() {
    }

    public PaymentStatusEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentStatusEntity)) return false;
        PaymentStatusEntity that = (PaymentStatusEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
