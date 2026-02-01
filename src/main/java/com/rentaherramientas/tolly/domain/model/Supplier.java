package com.rentaherramientas.tolly.domain.model;

import java.util.UUID;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;

public class Supplier {

    private Long id;
    private UUID userId;
    private String phone;
    private String company;

    // Constructor privado
    private Supplier() {}

    // Factory method
    public static Supplier create(UUID userId, String phone, String company) {

        if (userId == null) {
            throw new DomainException("userId es obligatorio");
        }

        if (!isValidPhone(phone)) {
            throw new DomainException("El teléfono debe tener 10 dígitos y comenzar en 3");
        }

        if (company == null || company.isBlank()) {
            throw new DomainException("La compañía es obligatoria para SUPPLIER");
        }

        Supplier supplier = new Supplier();
        supplier.userId = userId;
        supplier.phone = phone.trim();
        supplier.company = company.trim();

        return supplier;
    }

    //
    private static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        phone = phone.trim();
        return phone.matches("^3\\d{9}$");
    }

    public void changePhone(String newPhone) {
    if (!isValidPhone(newPhone)) {
        throw new DomainException("Teléfono inválido");
    }
    this.phone = newPhone.trim();
    }

    public void changeCompany(String newCompany) {
    if (newCompany == null || newCompany.isBlank()) {
        throw new DomainException("La compañía no puede estar vacía");
    }
    this.company = newCompany.trim();
    }

    public Long getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getPhone() {
        return phone;
    }

    public String getCompany() {
        return company;
    }
}
