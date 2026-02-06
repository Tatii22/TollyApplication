package com.rentaherramientas.tolly.infrastructure.persistence.adapters.in.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rentaherramientas.tolly.application.dto.returns.CreateReturnRequest;
import com.rentaherramientas.tolly.application.dto.returns.ReturnResponse;
import com.rentaherramientas.tolly.application.dto.returns.UpdateReturnRequest;
import com.rentaherramientas.tolly.application.usecase.returns.CreateReturnUseCase;
import com.rentaherramientas.tolly.application.usecase.returns.DeleteReturnUseCase;
import com.rentaherramientas.tolly.application.usecase.returns.GetReturnByIdUseCase;
import com.rentaherramientas.tolly.application.usecase.returns.GetReturnsUseCase;
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

    public ReturnController(
        CreateReturnUseCase createReturnUseCase,
        GetReturnsUseCase getReturnsUseCase,
        GetReturnByIdUseCase getReturnByIdUseCase,
        UpdateReturnUseCase updateReturnUseCase,
        DeleteReturnUseCase deleteReturnUseCase) {
        this.createReturnUseCase = createReturnUseCase;
        this.getReturnsUseCase = getReturnsUseCase;
        this.getReturnByIdUseCase = getReturnByIdUseCase;
        this.updateReturnUseCase = updateReturnUseCase;
        this.deleteReturnUseCase = deleteReturnUseCase;
    }

    @GetMapping
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('CLIENT') or hasRole('ADMIN')")
    @Operation(summary = "Listar devoluciones", description = "Retorna todas las devoluciones")
    @ApiResponse(responseCode = "200", description = "Lista de devoluciones obtenida exitosamente")
    public ResponseEntity<List<ReturnResponse>> getAll() {
        return ResponseEntity.ok(getReturnsUseCase.execute());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPPLIER') or hasRole('CLIENT') or hasRole('ADMIN')")
    @Operation(summary = "Obtener devolucion por ID", description = "Retorna una devolucion por ID")
    @ApiResponse(responseCode = "200", description = "Devolucion obtenida exitosamente")
    public ResponseEntity<ReturnResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(getReturnByIdUseCase.execute(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Crear devolucion", description = "Crea una devolucion")
    @ApiResponse(responseCode = "201", description = "Devolucion creada exitosamente")
    public ResponseEntity<ReturnResponse> create(@Valid @RequestBody CreateReturnRequest request) {
        ReturnResponse response = createReturnUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPPLIER')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Actualizar devolucion", description = "Actualiza una devolucion")
    @ApiResponse(responseCode = "200", description = "Devolucion actualizada exitosamente")
    public ResponseEntity<ReturnResponse> update(
        @PathVariable Long id,
        @Valid @RequestBody UpdateReturnRequest request) {
        return ResponseEntity.ok(updateReturnUseCase.execute(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPPLIER')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Eliminar devolucion", description = "Elimina una devolucion")
    @ApiResponse(responseCode = "204", description = "Devolucion eliminada exitosamente")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteReturnUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
