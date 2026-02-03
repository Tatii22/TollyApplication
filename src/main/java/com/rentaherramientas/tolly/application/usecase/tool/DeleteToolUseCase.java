package com.rentaherramientas.tolly.application.usecase.tool;

import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.ports.ToolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UseCase para eliminar una herramienta
 */
@Service
public class DeleteToolUseCase {
    
    private final ToolRepository toolRepository;
    
    public DeleteToolUseCase(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }
    
    @Transactional
    public void execute(Long id) {
        // Verificar que la herramienta existe
        toolRepository.findById(id)
            .orElseThrow(() -> new DomainException("Herramienta con ID " + id + " no encontrada"));
        
        // Eliminar la herramienta
        toolRepository.deleteById(id);
    }
}
