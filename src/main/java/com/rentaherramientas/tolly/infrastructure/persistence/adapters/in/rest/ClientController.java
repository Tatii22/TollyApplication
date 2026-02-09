package com.rentaherramientas.tolly.infrastructure.persistence.adapters.in.rest;

import com.rentaherramientas.tolly.application.dto.ClientResponse;
import com.rentaherramientas.tolly.domain.ports.ClientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clients", description = "Gestión de clientes")
public class ClientController {

    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    // Obtener cliente por ID (solo ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Obtener cliente por ID", description = "Retorna los datos de un cliente por su ID")
    @ApiResponse(responseCode = "200", description = "Cliente obtenido exitosamente")
    @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable Long id) {
        return clientRepository.findById(id)
            .map(client -> ResponseEntity.ok(ClientResponse.fromDomain(client)))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
