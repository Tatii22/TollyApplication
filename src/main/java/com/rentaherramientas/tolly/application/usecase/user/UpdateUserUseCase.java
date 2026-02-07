package com.rentaherramientas.tolly.application.usecase.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.application.dto.UserFullResponse;
import com.rentaherramientas.tolly.application.dto.auth.UpdateUserRequest;
import com.rentaherramientas.tolly.application.mapper.UserMapper;
import com.rentaherramientas.tolly.domain.exceptions.UserNotFoundException;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Client;
import com.rentaherramientas.tolly.domain.model.Supplier;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.domain.ports.SupplierRepository;
import com.rentaherramientas.tolly.domain.ports.UserRepository;
import com.rentaherramientas.tolly.domain.ports.UserStatusRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UpdateUserUseCase {

  private final UserRepository userRepository;
  private final ClientRepository clientRepository;
  private final SupplierRepository supplierRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final UserStatusRepository userStatusRepository;

  public UpdateUserUseCase(UserRepository userRepository,
      ClientRepository clientRepository,
      SupplierRepository supplierRepository,
      UserMapper userMapper,
      PasswordEncoder passwordEncoder,
      UserStatusRepository userStatusRepository) {
    this.userRepository = userRepository;
    this.clientRepository = clientRepository;
    this.supplierRepository = supplierRepository;
    this.userMapper = userMapper;
    this.passwordEncoder = passwordEncoder;
    this.userStatusRepository = userStatusRepository;
  }

  @Transactional
public UserFullResponse execute(UpdateUserRequest request) {

    // 1️⃣ Buscar usuario por email
    User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new UserNotFoundException("Email: " + request.email()));

    // 2️⃣ Actualizar campos del User
    if (request.password() != null && !request.password().isBlank()) {
        user.changePassword(passwordEncoder.encode(request.password()));
    }

    if (request.email() != null && !request.email().isBlank()) {
        user.setEmail(request.email());
    }

    if (request.status() != null && request.status().name() != null && !request.status().name().isBlank()) {
        var status = userStatusRepository.findByName(request.status().name())
            .orElseThrow(() -> new DomainException("Estado de usuario no encontrado: " + request.status().name()));
        user.assignStatus(status);
    }

    // 3️⃣ Actualizar perfil de cliente si existe
    Client client = user.getClient();
    if (client != null) {
        if (request.firstName() != null && !request.firstName().isBlank()) {
            client.setFirstName(request.firstName());
        }
        if (request.lastName() != null && !request.lastName().isBlank()) {
            client.setLastName(request.lastName());
        }
        if (request.address() != null && !request.address().isBlank()) {
            client.changeAddress(request.address());
        }
        if (request.phone() != null && !request.phone().isBlank()) {
            client.changePhone(request.phone());
        }
        if (request.document() != null && !request.document().isBlank()) {
            client.setDocument(request.document());
        }
        clientRepository.save(client);
    }

    // 4️⃣ Actualizar perfil de proveedor si existe
    Supplier supplier = user.getSupplier();
    if (supplier != null) {
        if (request.phone() != null && !request.phone().isBlank()) {
            supplier.changePhone(request.phone());
        }
        if (request.company() != null && !request.company().isBlank()) {
            supplier.changeCompany(request.company());
        }
        if (request.identification() != null && !request.identification().isBlank()) {
            supplier.setIdentification(request.identification());
        }
        if (request.contactName() != null && !request.contactName().isBlank()) {
            supplier.setContactName(request.contactName());
        }
        supplierRepository.save(supplier);
    }

    // 5️⃣ Guardar cambios en User
    userRepository.save(user);

    // 6️⃣ Mapear a DTO de respuesta
    return userMapper.toResponse(user);
}
}
