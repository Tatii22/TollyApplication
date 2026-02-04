package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Entidad JPA para User
 * Esta es la representación de persistencia, separada del modelo de dominio
 */
@Entity
@Table(name = "users")
public class UserEntity {

  @Id
  @Column(columnDefinition = "CHAR(36)", name = "user_id")
  private UUID id;

  @Column(nullable = false, unique = true, length = 100)
  private String email;

  @Column(nullable = false, length = 255)
  private String password;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_status_id", nullable = false, columnDefinition = "CHAR(36)")
  private UserStatusEntity status;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<RoleEntity> roles = new HashSet<>();

  // 🔥 RELACIONES DE NEGOCIO
  @OneToOne(mappedBy = "userId", fetch = FetchType.LAZY)
  private ClientEntity client;

  @OneToOne(mappedBy = "userId", fetch = FetchType.LAZY)
  private SupplierEntity supplier;

  // Constructores
  public UserEntity() {
  }

  public UserEntity(
      UUID id,
      String email,
      String password,
      Set<RoleEntity> roles,
      UserStatusEntity status) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.roles = roles != null ? new HashSet<>(roles) : new HashSet<>();
    this.status = status;
  }

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

  public UserStatusEntity getStatus() {
    return status;
  }

  public void setStatus(UserStatusEntity status) {
    this.status = status;
  }

  public Set<RoleEntity> getRoles() {
    return roles;
  }

  public void setRoles(Set<RoleEntity> roles) {
    this.roles = roles;
  }

  public ClientEntity getClient() {
    return client;
  }

  public void setClient(ClientEntity client) {
    this.client = client;
  }

  public SupplierEntity getSupplier() {
    return supplier;
  }

  public void setSupplier(SupplierEntity supplier) {
    this.supplier = supplier;
  }

  // Getters y Setters

}
