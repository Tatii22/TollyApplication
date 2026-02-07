package com.rentaherramientas.tolly.application.usecase.auth;

import com.rentaherramientas.tolly.application.dto.ChangePasswordRequest;
import com.rentaherramientas.tolly.domain.exceptions.InvalidCredentialsException;
import com.rentaherramientas.tolly.domain.exceptions.UserNotFoundException;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.PasswordService;
import com.rentaherramientas.tolly.domain.ports.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Caso de uso: Cambiar contraseña
 */
@Service
public class ChangePasswordUseCase {
    
    private final UserRepository userRepository;
    private final PasswordService passwordService;
    
    public ChangePasswordUseCase(UserRepository userRepository, PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }
    
    @Transactional
    public void execute(UUID userId, ChangePasswordRequest request) {
        // Buscar usuario
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        
        // Verificar contraseña actual
        if (!passwordService.matches(request.currentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Contraseña actual incorrecta");
        }
        
        // Hashear nueva contraseña
        String hashedNewPassword = passwordService.hash(request.newPassword());
        
        // Cambiar contraseña
        user.changePassword(hashedNewPassword);
        
        // Guardar cambios
        userRepository.save(user);
    }
}
