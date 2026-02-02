package com.rentaherramientas.tolly.infrastructure.config;

import com.rentaherramientas.tolly.domain.model.Role;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.PasswordService;
import com.rentaherramientas.tolly.domain.ports.RoleRepository;
import com.rentaherramientas.tolly.domain.ports.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Inicializador de datos para roles
 * Crea los roles iniciales si no existen en la base de datos
 */
@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final PasswordService passwordService;

  public DataInitializer(RoleRepository roleRepository, UserRepository userRepository,PasswordService passwordService) {
    this.roleRepository = roleRepository;
    this.userRepository = userRepository;
    this.passwordService = passwordService;
  }

  @Override
  public void run(String... args) {
    initializeRoles();
  }

  private void initializeRoles() {
    logger.info("Inicializando roles...");

    // Crear rol USER
    if (!roleRepository.existsByAuthority("ROLE_USER")) {
      Role userRole = Role.reconstruct(
          UUID.fromString("550e8400-e29b-41d4-a716-446655440001"),
          "USER",
          "ROLE_USER");
      roleRepository.save(userRole);
      logger.info("Rol USER creado");
    }

    // Crear rol ADMIN
    if (!roleRepository.existsByAuthority("ROLE_ADMIN")) {
      Role adminRole = Role.reconstruct(
          UUID.fromString("550e8400-e29b-41d4-a716-446655440002"),
          "ADMIN",
          "ROLE_ADMIN");
      roleRepository.save(adminRole);
      logger.info("Rol ADMIN creado");
    }

    // Crear rol MODERATOR
    if (!roleRepository.existsByAuthority("ROLE_SUPPLIER")) {
      Role moderatorRole = Role.reconstruct(
          UUID.fromString("550e8400-e29b-41d4-a716-446655440003"),
          "SUPPLIER",
          "ROLE_SUPPLIER");
      roleRepository.save(moderatorRole);
      logger.info("Rol SUPPLIER creado");
    }

    if (!roleRepository.existsByAuthority("ROLE_CLIENT")) {
      Role moderatorRole = Role.reconstruct(
          UUID.fromString("550e8400-e29b-41d4-a716-446655440004"),
          "CLIENT",
          "ROLE_CLIENT");
      roleRepository.save(moderatorRole);
      logger.info("Rol CLIENT creado");
    }

    User admin = User.create("admintoll@example.com", passwordService.hash("123456"));
    Role adminRole = roleRepository.findByAuthority("ROLE_ADMIN").get();
    admin.assignRole(adminRole);
    userRepository.save(admin);

    logger.info("Inicialización de roles completada");
  }
}
