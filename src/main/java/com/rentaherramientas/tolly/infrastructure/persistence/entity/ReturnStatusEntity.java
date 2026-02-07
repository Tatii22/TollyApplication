package com.rentaherramientas.tolly.infrastructure.persistence.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "return_status")
public class ReturnStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_return_status")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public ReturnStatusEntity() {}

    public ReturnStatusEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
}
