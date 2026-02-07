package com.rentaherramientas.tolly.application.usecase.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.application.dto.UserFullResponse;
import com.rentaherramientas.tolly.application.mapper.UserMapper;
import com.rentaherramientas.tolly.domain.ports.UserRepository;

@Service
public class ListUsersUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public ListUsersUseCase(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserFullResponse> execute() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }
}
