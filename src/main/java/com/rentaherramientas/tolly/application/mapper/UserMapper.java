package com.rentaherramientas.tolly.application.mapper;

import com.rentaherramientas.tolly.application.dto.RoleResponse;
import com.rentaherramientas.tolly.application.dto.UserResponse;
import com.rentaherramientas.tolly.domain.model.Role;
import com.rentaherramientas.tolly.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


/**
 * Mapper MapStruct para conversión entre User (dominio) y UserResponse (DTO)
 *
 * Con componentModel = "spring", se inyecta como bean de Spring.
 * No usar INSTANCE estático con este modelo.
 */
@Mapper(componentModel = "spring")
public interface UserMapper{

    /**
     * Convierte un User del dominio a UserResponse DTO
     */
    @Mapping(target = "roles", source = "roles")
    UserResponse toResponse(User user);

    /**
     * Convierte una lista de Roles del dominio a lista de RoleResponse DTOs
     */
    java.util.List<RoleResponse> rolesToRoleResponses(java.util.Set<Role> roles);

    /**
     * Convierte un Role del dominio a RoleResponse DTO
     */
    RoleResponse toRoleResponse(Role role);
}
