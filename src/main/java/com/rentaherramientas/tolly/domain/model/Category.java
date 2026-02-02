package com.rentaherramientas.tolly.domain.model;

public class Category {
    private Long id;
    private String name;

    public Category() {
    }

    public Category(Long id,
                    String name) {
                        
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede ser nulo o vacío");
        }
        this.id = id;
        this.name = name;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
}
