package com.rentaherramientas.tolly.infrastructure.persistence.adapters.in.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rentaherramientas.tolly.application.dto.returns.CreateReturnRequest;
import com.rentaherramientas.tolly.application.dto.returns.ReceiveReturnRequest;
import com.rentaherramientas.tolly.application.dto.returns.ReturnResponse;
import com.rentaherramientas.tolly.application.dto.returns.UpdateReturnRequest;
import com.rentaherramientas.tolly.application.usecase.returns.ConfirmReturnUseCase;
import com.rentaherramientas.tolly.application.usecase.returns.CreateReturnUseCase;
import com.rentaherramientas.tolly.application.usecase.returns.DeleteReturnUseCase;
import com.rentaherramientas.tolly.application.usecase.returns.GetReturnByIdUseCase;
import com.rentaherramientas.tolly.application.usecase.returns.GetReturnsUseCase;
import com.rentaherramientas.tolly.application.usecase.returns.ReceiveReturnUseCase;
import com.rentaherramientas.tolly.application.usecase.returns.UpdateReturnUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/returns")
@Tag(name = "Returns", description = "Gestion de devoluciones")
public class ReturnController {

    private final CreateReturnUseCase createReturnUseCase;
    private final GetReturnsUseCase getReturnsUseCase;
    private final GetReturnByIdUseCase getReturnByIdUseCase;
    private final UpdateReturnUseCase updateReturnUseCase;
    private final DeleteReturnUseCase deleteReturnUseCase;
    private final ConfirmReturnUseCase confirmReturnUseCase;
    private final ReceiveReturnUseCase receiveReturnUseCase;

    public ReturnController(
        CreateReturnUseCase createReturnUseCase,
        GetReturnsUseCase getReturnsUseCase,
        GetReturnByIdUseCase getReturnByIdUseCase,
        UpdateReturnUseCase updateReturnUseCase,
        DeleteReturnUseCase deleteReturnUseCase,
        ConfirmReturnUseCase confirmReturnUseCase,
        ReceiveReturnUseCase receiveReturnUseCase) {
        this.createReturnUseCase = createReturnUseCase;
        this.getReturnsUseCase = getReturnsUseCase;
        this.getReturnByIdUseCase = getReturnByIdUseCase;
        this.updateReturnUseCase = updateReturnUseCase;
        this.deleteReturnUseCase = deleteReturnUseCase;
        this.confirmReturnUseCase = confirmReturnUseCase;
        this.receiveReturnUseCase = receiveReturnUseCase;
    }

    @GetMapping
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('CLIENT') or hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Listar devoluciones", description = "Retorna todas las devoluciones")
    @ApiResponse(responseCode = "200", description = "Lista de devoluciones obtenida exitosamente")
    public ResponseEntity<List<ReturnResponse>> getAll() {
        return ResponseEntity.ok(getReturnsUseCase.execute());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('CLIENT') or hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Obtener devolucion por ID", description = "Retorna una devolucion por ID")
    @ApiResponse(responseCode = "200", description = "Devolucion obtenida exitosamente")
    public ResponseEntity<ReturnResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(getReturnByIdUseCase.execute(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Crear devolucion", description = "Crea una devolucion (clientId se toma del token)")
    @ApiResponse(responseCode = "201", description = "Devolucion creada exitosamente")
    public ResponseEntity<ReturnResponse> create(
        @Valid @RequestBody CreateReturnRequest request,
        Authentication authentication) {
        java.util.UUID userId = (java.util.UUID) authentication.getPrincipal();
        ReturnResponse response = createReturnUseCase.execute(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPPLIER','ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Actualizar devolucion", description = "Actualiza una devolucion")
    @ApiResponse(responseCode = "200", description = "Devolucion actualizada exitosamente")
    public ResponseEntity<ReturnResponse> update(
        @PathVariable Long id,
        @Valid @RequestBody UpdateReturnRequest request) {
        return ResponseEntity.ok(updateReturnUseCase.execute(id, request));
    }

    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasRole('CLIENT')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Confirmar devolucion", description = "El cliente confirma el envio de la devolucion")
    @ApiResponse(responseCode = "200", description = "Devolucion confirmada exitosamente")
    public ResponseEntity<ReturnResponse> confirm(
        @PathVariable Long id,
        Authentication authentication) {
        java.util.UUID userId = (java.util.UUID) authentication.getPrincipal();
        return ResponseEntity.ok(confirmReturnUseCase.execute(id, userId));
    }

    @PutMapping("/{id}/receive")
    @PreAuthorize("hasAnyRole('SUPPLIER','ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Recibir devolucion", description = "El proveedor recibe la devolucion")
    @ApiResponse(responseCode = "200", description = "Devolucion recibida exitosamente")
    public ResponseEntity<ReturnResponse> receive(
        @PathVariable Long id,
        @Valid @RequestBody ReceiveReturnRequest request,
        Authentication authentication) {
        boolean isAdmin = hasRole(authentication, "ROLE_ADMIN");
        java.util.UUID userId = isAdmin ? null : (java.util.UUID) authentication.getPrincipal();
        return ResponseEntity.ok(receiveReturnUseCase.execute(id, request, userId, isAdmin));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPPLIER','ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Eliminar devolucion", description = "Elimina una devolucion")
    @ApiResponse(responseCode = "204", description = "Devolucion eliminada exitosamente")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteReturnUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    private boolean hasRole(Authentication authentication, String role) {
        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
            .anyMatch(authority -> role.equals(authority.getAuthority()));
    }
}
