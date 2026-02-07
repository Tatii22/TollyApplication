package com.rentaherramientas.tolly.domain.model;

import com.rentaherramientas.tolly.domain.exceptions.DomainException;

public class Client {

  private Long id;
  private User userId;
  private String address;
  private String phone;
  private String FirstName;
  private String lastName;
  private String document;

  // Constructor privado
  private Client() {
  }

  private Client(Long id, User userId, String address, String phoneNumber, String name, String lastName,
      String document) {
    this.id = id;
    this.userId = userId;
    this.address = address;
    this.phone = phoneNumber;
    this.FirstName = name;
    this.lastName = lastName;
    this.document = document;
  }

  public static Client create(User userId, String address, String phoneNumber, String name, String lastName,
      String document) {

    if (userId == null) {
      throw new DomainException("userId es obligatorio");
    }

    if (address == null || address.isBlank()) {
      throw new DomainException("La dirección es obligatoria para CLIENT");
    }

    Client client = new Client();
    client.id = null;
    client.userId = userId;
    client.address = address.trim();
    client.phone = phoneNumber.trim();
    client.FirstName = name.trim();
    client.lastName = lastName.trim();
    client.document = document.trim();

    return client;
  }

  // Métodos de negocio
  public void changeAddress(String newAddress) {
    if (newAddress == null || newAddress.isBlank()) {
      throw new DomainException("La dirección no puede estar vacía");
    }
    this.address = newAddress.trim();
  }

  public void existsByDocument(String document) {
    if (document == null || document.isBlank()) {
      throw new DomainException("El número de documento es obligatorio");
    }
  }

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

  public Long getId() {
    return id;
  }

  public User getUserId() {
    return userId;
  }

  public String getAddress() {
    return address;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUserId(User userId) {
    this.userId = userId;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phoneNumber) {
    this.phone = phoneNumber;
  }

  public String getFirstName() {
    return FirstName;
  }

  public void setFirstName(String name) {
    this.FirstName = name;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getDocument() {
    return document;
  }

  public void setDocument(String document) {
    this.document = document;
  }

  public static Client restore(Long id, User userId, String address, String phoneNumber, String name, String lastName,
      String document) {
    return new Client(id, userId, address, phoneNumber, name, lastName, document);
  }
}
