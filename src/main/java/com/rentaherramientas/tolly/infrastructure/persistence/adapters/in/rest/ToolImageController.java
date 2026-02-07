package com.rentaherramientas.tolly.infrastructure.persistence.adapters.in.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import com.rentaherramientas.tolly.application.dto.tool.CreateToolImageRequest;
import com.rentaherramientas.tolly.application.dto.tool.ToolImageResponse;
import com.rentaherramientas.tolly.application.usecase.toolimage.CreateToolImageUseCase;
import com.rentaherramientas.tolly.application.usecase.toolimage.DeleteToolImageUseCase;
import com.rentaherramientas.tolly.application.usecase.toolimage.GetToolImageByIdUseCase;
import com.rentaherramientas.tolly.application.usecase.toolimage.GetToolImagesByToolIdUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Imágenes de herramientas", description = "Endpoints para gestión de imágenes de herramientas")
@RestController
@RequestMapping
public class ToolImageController {

    private final CreateToolImageUseCase createToolImageUseCase;
    private final GetToolImagesByToolIdUseCase getToolImagesByToolIdUseCase;
    private final GetToolImageByIdUseCase getToolImageByIdUseCase;
    private final DeleteToolImageUseCase deleteToolImageUseCase;

    public ToolImageController(
            CreateToolImageUseCase createToolImageUseCase,
            GetToolImagesByToolIdUseCase getToolImagesByToolIdUseCase,
            GetToolImageByIdUseCase getToolImageByIdUseCase,
            DeleteToolImageUseCase deleteToolImageUseCase) {
        this.createToolImageUseCase = createToolImageUseCase;
        this.getToolImagesByToolIdUseCase = getToolImagesByToolIdUseCase;
        this.getToolImageByIdUseCase = getToolImageByIdUseCase;
        this.deleteToolImageUseCase = deleteToolImageUseCase;
    }

    @Operation(summary = "Crear imagen de herramienta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Imagen creada exitosamente",
            content = @Content(schema = @Schema(implementation = ToolImageResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/tool-images")
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<ToolImageResponse> create(@RequestBody CreateToolImageRequest request) {
        ToolImageResponse response = createToolImageUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar imágenes por herramienta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Imágenes obtenidas exitosamente",
            content = @Content(schema = @Schema(implementation = ToolImageResponse.class)))
    })
    @GetMapping("/tools/{toolId}/images")
    public ResponseEntity<List<ToolImageResponse>> findByToolId(@PathVariable Long toolId) {
        List<ToolImageResponse> images = getToolImagesByToolIdUseCase.execute(toolId);
        return ResponseEntity.ok(images);
    }

    @Operation(summary = "Obtener imagen por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Imagen obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = ToolImageResponse.class))),
        @ApiResponse(responseCode = "404", description = "Imagen no encontrada")
    })
    @GetMapping("/tool-images/{id}")
    public ResponseEntity<ToolImageResponse> findById(@PathVariable Long id) {
        ToolImageResponse image = getToolImageByIdUseCase.execute(id);
        return ResponseEntity.ok(image);
    }

    @Operation(summary = "Eliminar imagen por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Imagen eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Imagen no encontrada")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/tool-images/{id}")
    @PreAuthorize("hasRole('SUPPLIER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteToolImageUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
