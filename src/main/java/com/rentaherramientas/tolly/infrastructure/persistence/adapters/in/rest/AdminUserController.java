package com.rentaherramientas.tolly.infrastructure.persistence.adapters.in.rest;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.rentaherramientas.tolly.application.dto.UserFullResponse;
import com.rentaherramientas.tolly.application.dto.auth.RegisterRequest;
import com.rentaherramientas.tolly.application.dto.auth.UpdateUserRequest;
import com.rentaherramientas.tolly.application.usecase.user.CreateUserUseCase;
import com.rentaherramientas.tolly.application.usecase.user.FindUserByEmailUseCase;
import com.rentaherramientas.tolly.application.usecase.user.ListUsersUseCase;
import com.rentaherramientas.tolly.application.usecase.user.UpdateUserUseCase;
import com.rentaherramientas.tolly.application.usecase.user.DeleteUserUseCase;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

  private final CreateUserUseCase createUserUseCase;
  private final FindUserByEmailUseCase findUserByEmailUseCase;
  private final ListUsersUseCase listUsersUseCase;
  private final UpdateUserUseCase updateUserUseCase;
  private final DeleteUserUseCase deleteUserUseCase;

  public AdminUserController(CreateUserUseCase createUserUseCase, FindUserByEmailUseCase findUserByEmailUseCase,
      ListUsersUseCase listUsersUseCase, UpdateUserUseCase updateUserUseCase,
      DeleteUserUseCase deleteUserUseCase) {
    this.createUserUseCase = createUserUseCase;
    this.findUserByEmailUseCase = findUserByEmailUseCase;
    this.listUsersUseCase = listUsersUseCase;
    this.updateUserUseCase = updateUserUseCase;
    this.deleteUserUseCase = deleteUserUseCase;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/by-email")
  public UserFullResponse getUserByEmail(@RequestParam String email) {
    return findUserByEmailUseCase.execute(email);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/clients")
  public UserFullResponse createClient(@RequestBody RegisterRequest request) {
    // Fuerza el rol a CLIENT
    RegisterRequest clientRequest = new RegisterRequest(
        request.email(),
    request.password(),
    "CLIENT",          // role
    request.firstName(),  // firstName
    request.lastName(),   // lastName
    request.address(),    // address
    request.national(),   // national
    request.phone(),      // phone
    null,                 // company
    null,                 // identification
    null
    );
    return createUserUseCase.execute(clientRequest, true);
  }

  @PutMapping("/update")
  @PreAuthorize("hasRole('ADMIN')")
  public UserFullResponse updateUser(
      @RequestBody UpdateUserRequest request) {
    return updateUserUseCase.execute(request);
  }

  @GetMapping("/list")
  @PreAuthorize("hasRole('ADMIN')")
  public List<UserFullResponse> listUsers() {
    return listUsersUseCase.execute();
  }

  @GetMapping("/clients")
  @PreAuthorize("hasRole('ADMIN')")
  public List<UserFullResponse> listClients() {
    return listUsersUseCase.execute().stream()
        .filter(user -> user.client() != null)
        .toList();
  }

  @GetMapping("/suppliers")
  @PreAuthorize("hasRole('ADMIN')")
  public List<UserFullResponse> listSuppliers() {
    return listUsersUseCase.execute().stream()
        .filter(user -> user.supplier() != null)
        .toList();
  }

  @DeleteMapping("/{userId}")
  @PreAuthorize("hasRole('ADMIN')")
  public void deleteUser(@PathVariable java.util.UUID userId) {
    deleteUserUseCase.execute(userId);
  }

  @PostMapping("/suppliers")
  @PreAuthorize("hasRole('ADMIN')")
  public UserFullResponse createSupplier(@RequestBody RegisterRequest request) {
    // Fuerza el rol a SUPPLIER
    RegisterRequest supplierRequest = new RegisterRequest(
    request.email(),
    request.password(),
    "SUPPLIER",  // role
    null,                 // firstName
    null,                 // lastName
    null,                 // address
    null,                 // national
    request.phone(),      // phone
    request.company(),    // company
    request.identification(), // identification
    request.contactName()     //
    );
    return createUserUseCase.execute(supplierRequest, true);
  }
}
