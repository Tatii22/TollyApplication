package com.rentaherramientas.tolly.domain.model;

import java.util.UUID;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;

public class Supplier {

    private UUID id;
    private UUID userId;
    private String phone;
    private String company;

    // Constructor privado
    private Supplier() {}



    public Supplier(UUID id, UUID userId, String phone, String company) {
      this.id = id;
      this.userId = userId;
      this.phone = phone;
      this.company = company;
    }



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
        supplier.id = UUID.randomUUID();
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

    public UUID getId() {
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

    public static Supplier restore(UUID id, UUID userId, String phone, String company) {
      return new Supplier(id, userId, phone, company);
}

}
