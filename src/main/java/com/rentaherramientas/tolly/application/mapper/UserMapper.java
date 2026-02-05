package com.rentaherramientas.tolly.application.mapper;

import com.rentaherramientas.tolly.application.dto.RoleResponse;
import com.rentaherramientas.tolly.application.dto.UserFullResponse;
import com.rentaherramientas.tolly.application.dto.UserStatusResponse;
import com.rentaherramientas.tolly.application.dto.ClientResponse;
import com.rentaherramientas.tolly.application.dto.SupplierResponse;

import com.rentaherramientas.tolly.domain.model.Role;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.domain.model.UserStatus;

import java.util.Set;
import java.util.List;

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

  @Mapping(target = "roles", source = "roles")
  @Mapping(target = "client", expression = "java(user.getClient() != null ? new ClientResponse(user.getClient().getAddress(), user.getClient().getPhone(), user.getClient().getFirstName(), user.getClient().getLastName(), user.getClient().getDocument()) : null)")
  @Mapping(target = "supplier", expression = "java(user.getSupplier() != null ? new SupplierResponse(user.getSupplier().getPhone(), user.getSupplier().getCompany(), user.getSupplier().getContactName(), user.getSupplier().getIdentification()) : null)")

  UserFullResponse toResponse(User user);

  java.util.List<RoleResponse> rolesToRoleResponses(Set<Role> roles);

  RoleResponse toRoleResponse(Role role);

  @Mapping(target = "name", source = "statusName")
  UserStatusResponse toUserStatusResponse(UserStatus status);

}
