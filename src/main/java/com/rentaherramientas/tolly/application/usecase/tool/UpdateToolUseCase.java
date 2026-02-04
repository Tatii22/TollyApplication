package com.rentaherramientas.tolly.application.usecase.tool;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.rentaherramientas.tolly.domain.ports.ToolRepository;
import com.rentaherramientas.tolly.domain.ports.SupplierRepository;
import com.rentaherramientas.tolly.domain.model.Tool;
import com.rentaherramientas.tolly.domain.model.Supplier;
import com.rentaherramientas.tolly.application.mapper.ToolMapper;
import com.rentaherramientas.tolly.application.dto.tool.UpdateToolRequest;
import com.rentaherramientas.tolly.application.dto.tool.ToolResponse;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import java.util.UUID;

/**
 * UseCase para actualizar una herramienta
 * Solo el SUPPLIER propietario puede actualizar sus propias herramientas
 */
@Service
public class UpdateToolUseCase {
    private final ToolRepository toolRepository;
    private final SupplierRepository supplierRepository;
    private final ToolMapper toolMapper;
    
    public UpdateToolUseCase(ToolRepository toolRepository, SupplierRepository supplierRepository, ToolMapper toolMapper) {
        this.toolRepository = toolRepository;
        this.supplierRepository = supplierRepository;
        this.toolMapper = toolMapper;
    }
    
    @Transactional
    public ToolResponse execute(Long id, UpdateToolRequest request, UUID userId) {
        // Obtener la herramienta
        Tool existing = toolRepository.findById(id)
            .orElseThrow(() -> new DomainException("Herramienta con ID " + id + " no encontrada"));
        
        // Obtener el supplier del usuario logueado
        Supplier supplier = supplierRepository.findByUserId(userId)
            .orElseThrow(() -> new DomainException("Usuario no tiene perfil de SUPPLIER"));
        
        // Validar que la herramienta pertenece al supplier logueado
        if (!existing.getSupplierId().equals(supplier.getId())) {
            throw new DomainException("No tienes permiso para actualizar esta herramienta");
        }
        
        // Convertir request a Tool y actualizar
        Tool updatedTool = toolMapper.toTool(request);
        updatedTool.setId(id); // Mantener el ID original
        
        Tool saved = toolRepository.update(id, updatedTool)
            .orElseThrow(() -> new DomainException("Error al actualizar la herramienta con ID " + id));
        
        return toolMapper.toToolResponse(saved);
    }
}

