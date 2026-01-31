package com.rentaherramientas.tolly.application.usecase.user;

import com.rentaherramientas.tolly.application.dto.UserResponse;
import com.rentaherramientas.tolly.application.dto.auth.RegisterRequest;
import com.rentaherramientas.tolly.application.mapper.UserMapper;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Role;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.PasswordService;
import com.rentaherramientas.tolly.domain.ports.RoleRepository;
import com.rentaherramientas.tolly.domain.ports.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Caso de uso: Crear usuario
 * Asigna ROL_USER por defecto desde la base de datos
 */
@Service
public class CreateUserUseCase {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordService passwordService;
    private final UserMapper userMapper;
    
    public CreateUserUseCase(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordService passwordService,
            UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordService = passwordService;
        this.userMapper = userMapper;
    }
    
    @Transactional
    public UserResponse execute(RegisterRequest request) {
        // Verificar si el usuario ya existe
        if (userRepository.existsByEmail(request.email())) {
            throw new DomainException("El usuario con email " + request.email() + " ya existe");
        }
        
        // Hashear la contraseña
        String hashedPassword = passwordService.hash(request.password());
        
        // Crear el usuario
        User user = User.create(request.email(), hashedPassword);
        
        // Buscar y asignar rol USER por defecto
        Role userRole = roleRepository.findByAuthority("ROLE_USER")
            .orElseThrow(() -> new DomainException("Rol USER no encontrado en la base de datos"));
        user.assignRole(userRole);
        
        // Guardar
        User savedUser = userRepository.save(user);
        
        // Retornar DTO usando mapper
        return userMapper.toResponse(savedUser);
    }
}
