package com.rentaherramientas.tolly.application.usecase.user;

import com.rentaherramientas.tolly.application.dto.*;
import com.rentaherramientas.tolly.application.dto.auth.RegisterRequest;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.*;
import com.rentaherramientas.tolly.domain.ports.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CreateUserUseCase {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordService passwordService;
  private final ClientRepository clientRepository;
  private final SupplierRepository supplierRepository;
  private final UserStatusRepository userStatusRepository;

  public CreateUserUseCase(
      UserRepository userRepository,
      RoleRepository roleRepository,
      PasswordService passwordService,
      ClientRepository clientRepository,
      SupplierRepository supplierRepository,
      UserStatusRepository userStatusRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordService = passwordService;
    this.clientRepository = clientRepository;
    this.supplierRepository = supplierRepository;
    this.userStatusRepository = userStatusRepository;
  }

  @Transactional
public UserFullResponse execute(RegisterRequest request, boolean isAdmin) {

    // 1️⃣ Validar email único
    if (userRepository.existsByEmail(request.email())) {
        throw new DomainException("El usuario con email " + request.email() + " ya existe");
    }

    // 2️⃣ Crear User
    String hashedPassword = passwordService.hash(request.password());
    UserStatus activeStatus = userStatusRepository.findByName("ACTIVE")
            .orElseThrow(() -> new DomainException("Estado ACTIVE no encontrado"));

    User user = User.create(request.email(), hashedPassword, activeStatus);

    // 3️⃣ Validar rol permitido según contexto
    String requestedRole = request.role().toUpperCase();

    // Registro público (no admin) solo CLIENT
    if (!isAdmin && !requestedRole.equals("CLIENT")) {
        throw new DomainException("Solo se permite registro como CLIENT");
    }

    // Admin puede crear CLIENT o SUPPLIER
    if (!requestedRole.equals("CLIENT") && !requestedRole.equals("SUPPLIER")) {
        throw new DomainException("Rol inválido");
    }

    // 4️⃣ Asignar roles
    Role userRole = roleRepository.findByAuthority("ROLE_USER")
            .orElseThrow(() -> new DomainException("Rol USER no encontrado"));
    user.assignRole(userRole);

    Role businessRole = roleRepository.findByAuthority("ROLE_" + requestedRole)
            .orElseThrow(() -> new DomainException("Rol de negocio no encontrado"));
    user.assignRole(businessRole);

    // 5️⃣ Persistir User
    User savedUser = userRepository.save(user);

    // 6️⃣ Crear perfil de negocio y DTO final
    ClientResponse clientResponse = null;
    SupplierResponse supplierResponse = null;

    if (requestedRole.equals("CLIENT")) {

        if (request.firstName() == null || request.lastName() == null || request.address() == null) {
            throw new DomainException("Los campos firstName, lastName y address son obligatorios para CLIENT");
        }

        Client client = Client.create(savedUser,
                request.firstName(),
                request.lastName(),
                request.address(),
                request.national(),
                request.phone());
        clientRepository.save(client);

        clientResponse = new ClientResponse(
                client.getFirstName(),
                client.getLastName(),
                client.getAddress(),
                client.getDocument(),
                client.getPhone());

    } else { // SUPPLIER

        if (request.company() == null || request.identification() == null || request.contactName() == null) {
            throw new DomainException("Los campos company, identification y contactName son obligatorios para SUPPLIER");
        }

        Supplier supplier = Supplier.create(
                savedUser,
                request.phone(),
                request.company(),
                request.identification(),
                request.contactName());
        supplierRepository.save(supplier);

        supplierResponse = new SupplierResponse(
                supplier.getPhone(),
                supplier.getCompany(),
                supplier.getIdentification(),
                supplier.getContactName());
    }

    // 7️⃣ Construir roles para la respuesta
    Set<RoleResponse> rolesResponse = savedUser.getRoles().stream()
            .map(r -> new RoleResponse(r.getId(), r.getName(), r.getAuthority()))
            .collect(Collectors.toSet());

    // 8️⃣ Retornar DTO combinado
    return new UserFullResponse(
            savedUser.getId().toString(),
            savedUser.getEmail(),
            rolesResponse,
            new UserStatusResponse(savedUser.getStatus().getStatusName()),
            clientResponse,
            supplierResponse);
}

}
