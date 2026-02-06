package com.rentaherramientas.tolly.domain.ports;

import com.rentaherramientas.tolly.domain.model.Role;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de roles
 */
public interface RoleRepository {
    
    /**
     * Guarda un rol
     */
    Role save(Role role);
    
    /**
     * Busca un rol por nombre
     */
    Optional<Role> findByName(String name);
    
    /**
     * Busca un rol por authority
     */
    Optional<Role> findByAuthority(String authority);
    
    /**
     * Busca un rol por ID
     */
    Optional<Role> findById(Long id);
    
    /**
     * Verifica si existe un rol con el nombre dado
     */
    boolean existsByName(String name);
    
    /**
     * Verifica si existe un rol con el authority dado
     */
    boolean existsByAuthority(String authority);
}
