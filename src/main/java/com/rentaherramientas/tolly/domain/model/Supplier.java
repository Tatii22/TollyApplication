package com.rentaherramientas.tolly.domain.model;

import com.rentaherramientas.tolly.domain.exceptions.DomainException;

public class Supplier {

  private Long id;
  private User userId;
  private String identification;
  private String phone;
  private String contactName;
  private String company;

  // Constructor privado
  private Supplier() {
  }

  public Supplier(Long id, User userId, String phone, String company, String identification, String contactName) {
    this.id = id;
    this.userId = userId;
    this.phone = phone;
    this.company = company;
    this.identification = identification;
    this.contactName = contactName;
  }

  // Factory method
  public static Supplier create(User userId, String phone, String company, String identification, String contactName) {

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
    supplier.id = null;
    supplier.userId = userId;
    supplier.phone = phone.trim();
    supplier.company = company.trim();
    supplier.identification = identification.trim();
    supplier.contactName = contactName.trim();

    return supplier;
  }

  //
  private static boolean isValidPhone(String phone) {
    if (phone == null)
      return false;
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

  public static Supplier restore(Long id, User userId, String phone, String company, String identification,
      String contactName) {
    return new Supplier(id, userId, phone, company, identification, contactName);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUserId() {
    return userId;
  }

  public void setUserId(User userId) {
    this.userId = userId;
  }

  public String getIdentification() {
    return identification;
  }

  public void setIdentification(String identification) {
    this.identification = identification;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getContactName() {
    return contactName;
  }

  public void setContactName(String contactName) {
    this.contactName = contactName;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  

}
