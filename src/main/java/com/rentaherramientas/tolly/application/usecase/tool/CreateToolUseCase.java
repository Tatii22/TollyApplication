package com.rentaherramientas.tolly.application.usecase.tool;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.rentaherramientas.tolly.domain.ports.ToolRepository;
import com.rentaherramientas.tolly.domain.ports.SupplierRepository;
import com.rentaherramientas.tolly.domain.ports.ToolStatusRepository;
import com.rentaherramientas.tolly.domain.ports.CategoryRepository;
import com.rentaherramientas.tolly.domain.model.Tool;
import com.rentaherramientas.tolly.application.mapper.ToolMapper;
import com.rentaherramientas.tolly.application.dto.tool.CreateToolRequest;
import com.rentaherramientas.tolly.application.dto.tool.ToolResponse;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;

/**
 * UseCase para crear una nueva herramienta
 * - Valida que el supplierId existe
 * - Valida que el statusId existe
 * - Valida que no exista otra herramienta con el mismo nombre
 */
@Service
public class CreateToolUseCase {
    private final ToolRepository toolRepository;
    private final SupplierRepository supplierRepository;
    private final ToolStatusRepository toolStatusRepository;
    private final CategoryRepository categoryRepository;
    private final ToolMapper toolMapper;
    
    public CreateToolUseCase(ToolRepository toolRepository, SupplierRepository supplierRepository, 
                            ToolStatusRepository toolStatusRepository, CategoryRepository categoryRepository,
                            ToolMapper toolMapper) {
        this.toolRepository = toolRepository;
        this.supplierRepository = supplierRepository;
        this.toolStatusRepository = toolStatusRepository;
        this.categoryRepository = categoryRepository;
        this.toolMapper = toolMapper;
    }
    
    @Transactional
    public ToolResponse execute(CreateToolRequest request) {
        // Validar que el supplier existe
        supplierRepository.findById(request.supplierId())
            .orElseThrow(() -> new DomainException("Proveedor con ID " + request.supplierId() + " no existe"));
        
        // Validar que el status existe
        toolStatusRepository.findById(request.statusId())
            .orElseThrow(() -> new DomainException("Estado con ID " + request.statusId() + " no existe"));

        // Validar que la categoría existe
        categoryRepository.findById(request.categoryId())
            .orElseThrow(() -> new DomainException("Categoría con ID " + request.categoryId() + " no existe"));
        
        // Verificar si la herramienta ya existe con el mismo nombre
        if (toolRepository.existsByName(request.name())) {
            throw new DomainException("Ya existe una herramienta con el nombre: " + request.name());
        }
        
        // Crear la herramienta
        Tool tool = toolMapper.toTool(request);
        Tool saved = toolRepository.save(tool);
        
        return toolMapper.toToolResponse(saved);
    }
}
