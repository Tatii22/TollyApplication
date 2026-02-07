package com.rentaherramientas.tolly.application.usecase.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.exceptions.UserNotFoundException;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import com.rentaherramientas.tolly.domain.ports.SupplierRepository;
import com.rentaherramientas.tolly.domain.ports.UserRepository;

import java.util.UUID;

@Service
public class DeleteUserUseCase {

  private final UserRepository userRepository;
  private final ClientRepository clientRepository;
  private final SupplierRepository supplierRepository;

  public DeleteUserUseCase(UserRepository userRepository,
      ClientRepository clientRepository,
      SupplierRepository supplierRepository) {
    this.userRepository = userRepository;
    this.clientRepository = clientRepository;
    this.supplierRepository = supplierRepository;
  }

  @Transactional
  public void execute(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

    if (user.getClient() != null) {
      clientRepository.delete(user.getClient());
    }
    if (user.getSupplier() != null) {
      supplierRepository.delete(user.getSupplier());
    }

    userRepository.delete(user);
  }
}
