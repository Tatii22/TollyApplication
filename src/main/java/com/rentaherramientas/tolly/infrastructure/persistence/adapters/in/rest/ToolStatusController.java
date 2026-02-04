package com.rentaherramientas.tolly.infrastructure.persistence.adapters.in.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.rentaherramientas.tolly.application.usecase.tool.CreateToolStatusUseCase;
import com.rentaherramientas.tolly.application.usecase.tool.GetToolStatusesUseCase;
import com.rentaherramientas.tolly.application.dto.tool.CreateToolStatusRequest;
import com.rentaherramientas.tolly.application.dto.tool.ToolStatusResponse;
import java.util.List;

/**
 * Controller REST para gestionar estados de herramientas
 * - POST: Solo ADMIN
 * - GET: Público
 */
@RestController
@RequestMapping("/tool-statuses")
@Tag(name = "Tool Status", description = "Gestión de estados de herramientas")
public class ToolStatusController {
    
    private final CreateToolStatusUseCase createToolStatusUseCase;
    private final GetToolStatusesUseCase getToolStatusesUseCase;
    
    public ToolStatusController(CreateToolStatusUseCase createToolStatusUseCase, 
                              GetToolStatusesUseCase getToolStatusesUseCase) {
        this.createToolStatusUseCase = createToolStatusUseCase;
        this.getToolStatusesUseCase = getToolStatusesUseCase;
    }
    
    @GetMapping
    @Operation(summary = "Listar todos los estados de herramientas", 
              description = "Retorna una lista de todos los estados disponibles en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de estados obtenida exitosamente")
    public ResponseEntity<List<ToolStatusResponse>> getAllToolStatuses() {
        List<ToolStatusResponse> statuses = getToolStatusesUseCase.execute();
        return ResponseEntity.ok(statuses);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Crear nuevo estado de herramienta", 
              description = "Solo ADMIN puede crear nuevos estados globales del sistema")
    @ApiResponse(responseCode = "201", description = "Estado creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @ApiResponse(responseCode = "403", description = "No tienes permiso (requiere rol ADMIN)")
    public ResponseEntity<ToolStatusResponse> createToolStatus(@Valid @RequestBody CreateToolStatusRequest request) {
        ToolStatusResponse response = createToolStatusUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
