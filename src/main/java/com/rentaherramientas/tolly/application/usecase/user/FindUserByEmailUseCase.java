package com.rentaherramientas.tolly.application.usecase.user;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.domain.exceptions.UserNotFoundException;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.ports.UserRepository;
import com.rentaherramientas.tolly.application.dto.UserFullResponse;

import com.rentaherramientas.tolly.application.mapper.UserMapper;

@Service
public class FindUserByEmailUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public FindUserByEmailUseCase(
            UserRepository userRepository,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserFullResponse execute(String email) {

        // 1️⃣ Buscar usuario por email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        // 2️⃣ Convertir directamente a UserResponse usando el mapper
        return userMapper.toResponse(user);
    }
}

