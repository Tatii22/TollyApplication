package com.rentaherramientas.tolly.domain.model;

import com.rentaherramientas.tolly.domain.exceptions.DomainException;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Entidad de dominio User
 * No tiene dependencias de frameworks externos
 */
public class User {

  private UUID id;
  private String email;
  private String password;
  private Set<Role> roles;
  private UserStatus status;

  // Relaciones opcionales
  private Client client;
  private Supplier supplier;

  // Constructor privado para forzar uso de factory methods
  private User() {
    this.roles = new HashSet<>();
  }

  public User(UUID id){
    this.id = id;
  }

  /**
   * Factory method para crear un nuevo usuario
   */
  public static User create(String email, String password, UserStatus initialStatus) {
    if (email == null || email.isBlank()) {
      throw new DomainException("Email no puede estar vacío");
    }
    if (password == null || password.isBlank()) {
      throw new DomainException("Password no puede estar vacío");
    }
    if (!isValidEmail(email)) {
      throw new DomainException("Email inválido");
    }
    if (initialStatus == null) {
      throw new DomainException("El estado inicial es obligatorio");
    }

    User user = new User();
    user.id = UUID.randomUUID();
    user.email = email.toLowerCase().trim();
    user.password = password;
    user.assignStatus(initialStatus);
    return user;
  }

  /**
   * Factory method para reconstruir desde persistencia
   */
  public static User reconstruct(UUID id, String email, String password,
      Set<Role> roles, UserStatus status) {

    User user = new User();
    user.id = id;
    user.email = email;
    user.password = password;
    user.roles = new HashSet<>(roles);
    user.status = status;
    return user;
  }

  /**
   * Asigna un rol al usuario
   */
  public void assignRole(Role role) {
    if (role == null) {
      throw new DomainException("El rol no puede ser nulo");
    }
    this.roles.add(role);
  }

  /**
   * Asigna / cambia el estado del usuario
   */
  public void assignStatus(UserStatus userStatus) {
    if (userStatus == null) {
      throw new DomainException("El estado de usuario no puede ser nulo");
    }
    this.status = userStatus;
  }

  /**
   * Remueve el estado (uso controlado)
   */
  public void removeStatus() {
    this.status = null;
  }

  /**
   * Verifica si el usuario tiene un rol específico
   */
  public boolean hasRole(Role role) {
    return this.roles.contains(role);
  }

  /**
   * Verifica si el usuario tiene un estado específico
   */
  public boolean hasStatus(UserStatus userStatus) {
    return this.status != null && this.status.equals(userStatus);
  }

  /**
   * Cambia la contraseña del usuario
   */
  public void changePassword(String newPassword) {
    if (newPassword == null || newPassword.isBlank()) {
      throw new DomainException("La nueva contraseña no puede estar vacía");
    }
    this.password = newPassword;
  }

  // Validación simple de email
  private static boolean isValidEmail(String email) {
    return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
  }

  public static User restore(UUID id) {
    User user = new User();
    user.id = id;
    return user;
  }

  public boolean isActive() {
    return status != null && "ACTIVE".equals(status.getStatusName());
  }

  // Getters & setters

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public Supplier getSupplier() {
    return supplier;
  }

  public void setSupplier(Supplier supplier) {
    this.supplier = supplier;
  }
}
