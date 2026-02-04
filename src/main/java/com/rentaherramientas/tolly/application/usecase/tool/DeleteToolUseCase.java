package com.rentaherramientas.tolly.application.usecase.tool;

import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Supplier;
import com.rentaherramientas.tolly.domain.model.Tool;
import com.rentaherramientas.tolly.domain.ports.SupplierRepository;
import com.rentaherramientas.tolly.domain.ports.ToolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

/**
 * UseCase para eliminar una herramienta
 * Solo el SUPPLIER propietario puede eliminar sus propias herramientas
 */
@Service
public class DeleteToolUseCase {
    
    private final ToolRepository toolRepository;
    private final SupplierRepository supplierRepository;
    
    public DeleteToolUseCase(ToolRepository toolRepository, SupplierRepository supplierRepository) {
        this.toolRepository = toolRepository;
        this.supplierRepository = supplierRepository;
    }
    
    @Transactional
    public void execute(Long id, UUID userId) {
        // Obtener la herramienta
        Tool tool = toolRepository.findById(id)
            .orElseThrow(() -> new DomainException("Herramienta con ID " + id + " no encontrada"));
        
        // Obtener el supplier del usuario logueado
        Supplier supplier = supplierRepository.findByUserId(userId)
            .orElseThrow(() -> new DomainException("Usuario no tiene perfil de SUPPLIER"));
        
        // Validar que la herramienta pertenece al supplier logueado
        if (!tool.getSupplierId().equals(supplier.getId())) {
            throw new DomainException("No tienes permiso para eliminar esta herramienta");
        }
        
        // Eliminar la herramienta
        toolRepository.deleteById(id);
    }
}
