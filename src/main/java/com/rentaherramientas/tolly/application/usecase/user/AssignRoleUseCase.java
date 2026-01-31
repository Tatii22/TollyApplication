package com.rentaherramientas.tolly.application.usecase.user;

import com.rentaherramientas.tolly.application.dto.AssignRoleRequest;
import com.rentaherramientas.tolly.application.dto.UserResponse;
import com.rentaherramientas.tolly.application.mapper.UserMapper;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.exceptions.UserNotFoundException;
import com.rentaherramientas.tolly.domain.model.Role;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.RoleRepository;
import com.rentaherramientas.tolly.domain.ports.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Caso de uso: Asignar rol a usuario
 */
@Service
public class AssignRoleUseCase {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    
    public AssignRoleUseCase(
            UserRepository userRepository, 
            RoleRepository roleRepository,
            UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }
    
    @Transactional
    public UserResponse execute(UUID userId, AssignRoleRequest request) {
        // Buscar usuario
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        
        // Buscar rol por nombre o authority
        Role role = roleRepository.findByName(request.roleName())
            .or(() -> roleRepository.findByAuthority(request.roleName()))
            .orElseThrow(() -> new DomainException("Rol " + request.roleName() + " no encontrado"));
        
        // Asignar rol
        user.assignRole(role);
        
        // Guardar cambios
        User savedUser = userRepository.save(user);
        
        // Retornar DTO usando mapper
        return userMapper.toResponse(savedUser);
    }
}
