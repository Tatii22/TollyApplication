package com.rentaherramientas.tolly.infrastructure.persistence.adapters.in.rest;

import com.rentaherramientas.tolly.application.dto.tool.CreateToolRequest;
import com.rentaherramientas.tolly.application.dto.tool.ToolPublicResponse;
import com.rentaherramientas.tolly.application.dto.tool.ToolResponse;
import com.rentaherramientas.tolly.application.dto.tool.UpdateToolRequest;
import com.rentaherramientas.tolly.application.usecase.tool.CreateToolUseCase;
import com.rentaherramientas.tolly.application.usecase.tool.DeleteToolUseCase;
import com.rentaherramientas.tolly.application.usecase.tool.GetToolByIdUseCase;
import com.rentaherramientas.tolly.application.usecase.tool.GetToolsUseCase;
import com.rentaherramientas.tolly.application.usecase.tool.UpdateToolUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para gestión de herramientas
 * Validaciones:
 * - Solo SUPPLIERS pueden crear herramientas
 * - Las herramientas se asocian a un supplier y una category
 * - Estado inicial es AVAIBLE (disponible)
 * - Un proveedor solo puede gestionar sus propias herramientas
 * - La tabla tools mantiene integridad referencial
 */
@Tag(name = "Gestión de Herramientas", description = "Endpoints para administración de herramientas por proveedores")
@RestController
@RequestMapping("/tools")
public class ToolController {
    
    private final GetToolsUseCase getToolsUseCase;
    private final GetToolByIdUseCase getToolByIdUseCase;
    private final CreateToolUseCase createToolUseCase;
    private final UpdateToolUseCase updateToolUseCase;
    private final DeleteToolUseCase deleteToolUseCase;

    public ToolController(
            GetToolsUseCase getToolsUseCase,
            GetToolByIdUseCase getToolByIdUseCase,
            CreateToolUseCase createToolUseCase,
            UpdateToolUseCase updateToolUseCase,
            DeleteToolUseCase deleteToolUseCase) {
        this.getToolsUseCase = getToolsUseCase;
        this.getToolByIdUseCase = getToolByIdUseCase;
        this.createToolUseCase = createToolUseCase;
        this.updateToolUseCase = updateToolUseCase;
        this.deleteToolUseCase = deleteToolUseCase;
    }

    @Operation(summary = "Listar herramientas", description = "Obtiene la lista completa de herramientas. Permite filtrar por disponibilidad y categoría")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de herramientas obtenida exitosamente", 
                    content = @Content(schema = @Schema(oneOf = {ToolResponse.class, ToolPublicResponse.class})))
    })
    @GetMapping
    public ResponseEntity<List<?>> findAll(
            @RequestParam(name = "availableOnly", defaultValue = "false") boolean availableOnly,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            Authentication authentication) {
        if (isClient(authentication)) {
            List<ToolPublicResponse> tools = getToolsUseCase.executePublic(availableOnly, categoryId);
            return ResponseEntity.ok(tools);
        }

        List<ToolResponse> tools = getToolsUseCase.execute(availableOnly, categoryId);
        return ResponseEntity.ok(tools);
    }

    @Operation(summary = "Obtener herramienta por ID", description = "Obtiene los detalles de una herramienta específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Herramienta obtenida exitosamente", 
                    content = @Content(schema = @Schema(oneOf = {ToolResponse.class, ToolPublicResponse.class}))),
        @ApiResponse(responseCode = "404", description = "Herramienta no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(
            @PathVariable Long id,
            Authentication authentication) {
        if (isClient(authentication)) {
            ToolPublicResponse tool = getToolByIdUseCase.executePublic(id);
            return ResponseEntity.ok(tool);
        }

        ToolResponse tool = getToolByIdUseCase.execute(id);
        return ResponseEntity.ok(tool);
    }

    private boolean isClient(Authentication authentication) {
        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }

        return authentication.getAuthorities().stream()
            .anyMatch(authority -> "ROLE_CLIENT".equals(authority.getAuthority()));
    }

    @Operation(summary = "Crear nueva herramienta", 
            description = "Crea una nueva herramienta. Solo SUPPLIERS pueden crear herramientas. " +
                            "La herramienta se asocia automáticamente al supplier autenticado. " +
                            "El estado inicial es AVAILABLE (disponible).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Herramienta creada exitosamente", 
                    content = @Content(schema = @Schema(implementation = ToolResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o categoría/supplier no existe"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No tiene permisos (requiere rol SUPPLIER)")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<ToolResponse> create(
            @Valid @RequestBody CreateToolRequest request,
            Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        ToolResponse tool = createToolUseCase.execute(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(tool);
    }

    @Operation(summary = "Actualizar herramienta", 
                description = "Actualiza los datos de una herramienta existente. " +
                            "Solo el supplier propietario puede actualizar sus herramientas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Herramienta actualizada exitosamente", 
                    content = @Content(schema = @Schema(implementation = ToolResponse.class))),
        @ApiResponse(responseCode = "403", description = "No tiene permisos para actualizar esta herramienta"),
        @ApiResponse(responseCode = "404", description = "Herramienta no encontrada")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<ToolResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateToolRequest request,
            Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        ToolResponse tool = updateToolUseCase.execute(id, request, userId);
        return ResponseEntity.ok(tool);
    }

    @Operation(summary = "Eliminar herramienta", 
                description = "Elimina una herramienta. " +
                            "Solo el supplier propietario puede eliminar sus herramientas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Herramienta eliminada exitosamente"),
        @ApiResponse(responseCode = "403", description = "No tiene permisos para eliminar esta herramienta"),
        @ApiResponse(responseCode = "404", description = "Herramienta no encontrada")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication) {
        UUID userId = (UUID) authentication.getPrincipal();
        deleteToolUseCase.execute(id, userId);
        return ResponseEntity.noContent().build();
    }
}
