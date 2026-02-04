package com.rentaherramientas.tolly.application.usecase.user;

import com.rentaherramientas.tolly.application.dto.UserFullResponse;
import com.rentaherramientas.tolly.application.mapper.UserMapper;
import com.rentaherramientas.tolly.domain.exceptions.UserNotFoundException;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Caso de uso: Obtener usuario autenticado
 */
@Service
public class GetCurrentUserUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public GetCurrentUserUseCase(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserFullResponse execute(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        return userMapper.toResponse(user);
    }
}
