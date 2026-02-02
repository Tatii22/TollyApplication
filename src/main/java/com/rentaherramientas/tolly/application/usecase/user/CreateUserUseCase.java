package com.rentaherramientas.tolly.application.usecase.user;

import com.rentaherramientas.tolly.application.dto.UserResponse;
import com.rentaherramientas.tolly.application.dto.auth.RegisterRequest;
import com.rentaherramientas.tolly.application.mapper.UserMapper;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Client;
import com.rentaherramientas.tolly.domain.model.Role;
import com.rentaherramientas.tolly.domain.model.Supplier;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.domain.ports.PasswordService;
import com.rentaherramientas.tolly.domain.ports.RoleRepository;
import com.rentaherramientas.tolly.domain.ports.SupplierRepository;
import com.rentaherramientas.tolly.domain.ports.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateUserUseCase {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordService passwordService;
  private final UserMapper userMapper;
  private final ClientRepository clientRepository;
  private final SupplierRepository supplierRepository;

  public CreateUserUseCase(UserRepository userRepository,
      RoleRepository roleRepository,
      PasswordService passwordService,
      UserMapper userMapper,
      ClientRepository clientRepository,
      SupplierRepository supplierRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordService = passwordService;
    this.userMapper = userMapper;
    this.clientRepository = clientRepository;
    this.supplierRepository = supplierRepository;
  }

  @Transactional
  public UserResponse execute(RegisterRequest request) {

    // Verificar si el usuario ya existe
    if (userRepository.existsByEmail(request.email())) {
      throw new DomainException("El usuario con email " + request.email() + " ya existe");
    }

    // Hashear la contraseña
    String hashedPassword = passwordService.hash(request.password());

    // Crear usuario con email y contraseña
    User user = User.create(request.email(), hashedPassword);

    // Validar que el rol sea CLIENT o SUPPLIER
    String requestedRole = request.role().toUpperCase();
    if (!requestedRole.equals("CLIENT") && !requestedRole.equals("SUPPLIER")) {
      throw new DomainException("Solo se permite registro como CLIENT o SUPPLIER");
    }

    // Asignar rol USER por defecto
    Role userRole = roleRepository.findByAuthority("ROLE_USER")
        .orElseThrow(() -> new DomainException("Rol USER no encontrado"));
    user.assignRole(userRole);

    // Asignar rol de negocio según el tipo de usuario
    Role businessRole = roleRepository.findByAuthority("ROLE_" + requestedRole)
        .orElseThrow(() -> new DomainException("Rol de negocio no encontrado"));
    user.assignRole(businessRole);

    // Guardar el usuario en la base de datos
    User savedUser = userRepository.save(user);

    // Asociar datos específicos según rol y persistir en DB usando adapters
    switch (requestedRole) {
      case "CLIENT" -> {
        if (request.address() == null || request.address().isBlank()) {
          throw new DomainException("La dirección es obligatoria para CLIENT");
        }
        // Crear dominio
        Client client = Client.create(savedUser.getId(), request.address());
        // Guardar usando el adapter (convierte a entidad y persiste)
        clientRepository.save(client);
        // Asignar al user para el DTO
        savedUser.setClient(client);
      }
      case "SUPPLIER" -> {
        if (request.phone() == null || request.phone().isBlank()
            || request.company() == null || request.company().isBlank()) {
          throw new DomainException("Teléfono y compañía son obligatorios para SUPPLIER");
        }
        // Crear dominio
        Supplier supplier = Supplier.create(savedUser.getId(), request.phone(), request.company());
        // Guardar usando el adapter
        supplierRepository.save(supplier);
        // Asignar al user para el DTO
        savedUser.setSupplier(supplier);
      }
    }

    // Retornar DTO de usuario con roles y datos de negocio
    return userMapper.toResponse(savedUser);
  }
}
