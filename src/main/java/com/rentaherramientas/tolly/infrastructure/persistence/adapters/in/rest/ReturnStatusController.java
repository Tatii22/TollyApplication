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
import com.rentaherramientas.tolly.application.usecase.returnstatus.CreateReturnStatusUseCase;
import com.rentaherramientas.tolly.application.usecase.returnstatus.GetReturnStatusesUseCase;
import com.rentaherramientas.tolly.application.dto.returnstatus.CreateReturnStatusRequest;
import com.rentaherramientas.tolly.application.dto.returnstatus.ReturnStatusResponse;
import java.util.List;

/**
 * Controller REST para gestionar estados de devolucion
 * - POST: Solo ADMIN
 * - GET: Publico
 */
@RestController
@RequestMapping("/return-statuses")
@Tag(name = "Return Status", description = "Gestion de estados de devolucion")
public class ReturnStatusController {

    private final CreateReturnStatusUseCase createReturnStatusUseCase;
    private final GetReturnStatusesUseCase getReturnStatusesUseCase;

    public ReturnStatusController(CreateReturnStatusUseCase createReturnStatusUseCase,
                                  GetReturnStatusesUseCase getReturnStatusesUseCase) {
        this.createReturnStatusUseCase = createReturnStatusUseCase;
        this.getReturnStatusesUseCase = getReturnStatusesUseCase;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPLIER') or hasRole('CLIENT')")
    @Operation(summary = "Listar todos los estados de devolucion",
               description = "Retorna una lista de todos los estados disponibles en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de estados obtenida exitosamente")
    public ResponseEntity<List<ReturnStatusResponse>> getAllReturnStatuses() {
        List<ReturnStatusResponse> statuses = getReturnStatusesUseCase.execute();
        return ResponseEntity.ok(statuses);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Crear nuevo estado de devolucion",
              description = "Solo ADMIN puede crear nuevos estados globales del sistema")
    @ApiResponse(responseCode = "201", description = "Estado creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos invalidos")
    @ApiResponse(responseCode = "403", description = "No tienes permiso (requiere rol ADMIN)")
    public ResponseEntity<ReturnStatusResponse> createReturnStatus(@Valid @RequestBody CreateReturnStatusRequest request) {
        ReturnStatusResponse response = createReturnStatusUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
