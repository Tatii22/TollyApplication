package com.rentaherramientas.tolly.application.usecase.tool;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.rentaherramientas.tolly.domain.ports.ToolRepository;
import com.rentaherramientas.tolly.domain.ports.SupplierRepository;
import com.rentaherramientas.tolly.domain.ports.ToolStatusRepository;
import com.rentaherramientas.tolly.domain.ports.CategoryRepository;
import com.rentaherramientas.tolly.domain.model.Tool;
import com.rentaherramientas.tolly.domain.model.Supplier;
import com.rentaherramientas.tolly.application.mapper.ToolMapper;
import com.rentaherramientas.tolly.application.dto.tool.UpdateToolRequest;
import com.rentaherramientas.tolly.application.dto.tool.ToolResponse;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import java.util.UUID;

/**
 * UseCase para actualizar una herramienta
 * - Solo el SUPPLIER propietario puede actualizar sus propias herramientas
 * - Valida que el statusId existe
 */
@Service
public class UpdateToolUseCase {
    private final ToolRepository toolRepository;
    private final SupplierRepository supplierRepository;
    private final ToolStatusRepository toolStatusRepository;
    private final CategoryRepository categoryRepository;
    private final ToolMapper toolMapper;
    
    public UpdateToolUseCase(ToolRepository toolRepository, SupplierRepository supplierRepository, 
                            ToolStatusRepository toolStatusRepository, CategoryRepository categoryRepository,
                            ToolMapper toolMapper) {
        this.toolRepository = toolRepository;
        this.supplierRepository = supplierRepository;
        this.toolStatusRepository = toolStatusRepository;
        this.categoryRepository = categoryRepository;
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
        
        // Validar que el nuevo supplierId existe
        supplierRepository.findById(request.supplierId())
            .orElseThrow(() -> new DomainException("Proveedor con ID " + request.supplierId() + " no existe"));
        
        // Validar que el status existe
        toolStatusRepository.findById(request.statusId())
            .orElseThrow(() -> new DomainException("Estado con ID " + request.statusId() + " no existe"));

        // Validar que la categoría existe
        categoryRepository.findById(request.categoryId())
            .orElseThrow(() -> new DomainException("Categoría con ID " + request.categoryId() + " no existe"));
        
        // Validar transiciones de estado (flujo de reparacion)
        Long currentStatusId = existing.getStatusId();
        Long requestedStatusId = request.statusId();

        if (currentStatusId != null && requestedStatusId != null && !currentStatusId.equals(requestedStatusId)) {
            String currentStatusName = toolStatusRepository.findById(currentStatusId)
                .map(status -> status.getName().toUpperCase())
                .orElse(null);
            String nextStatusName = toolStatusRepository.findById(requestedStatusId)
                .map(status -> status.getName().toUpperCase())
                .orElse(null);

            if (currentStatusName != null && nextStatusName != null) {
                boolean involvesRepairFlow =
                    "UNAVAILABLE".equals(currentStatusName)
                    || "UNDER_REPAIR".equals(currentStatusName)
                    || "UNAVAILABLE".equals(nextStatusName)
                    || "UNDER_REPAIR".equals(nextStatusName);

                if (involvesRepairFlow) {
                    boolean validRepairFlow =
                        ("UNAVAILABLE".equals(currentStatusName) && "UNDER_REPAIR".equals(nextStatusName))
                        || ("UNDER_REPAIR".equals(currentStatusName) && "AVAILABLE".equals(nextStatusName));
                    if (!validRepairFlow) {
                        throw new DomainException("Transicion de estado no permitida: " + currentStatusName + " -> " + nextStatusName);
                    }
                }
            }
        }

        // Convertir request a Tool y actualizar
        Tool updatedTool = toolMapper.toTool(request);
        updatedTool.setId(id); // Mantener el ID original
        
        Tool saved = toolRepository.update(id, updatedTool)
            .orElseThrow(() -> new DomainException("Error al actualizar la herramienta con ID " + id));
        
        return toolMapper.toToolResponse(saved);
    }
}
