package com.rentaherramientas.tolly.application.usecase.category;

import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.ports.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UseCase para eliminar una categoría
 */
@Service
public class DeleteCategoryUseCase {
    
    private final CategoryRepository categoryRepository;
    
    public DeleteCategoryUseCase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    @Transactional
    public void execute(Long id) {
        // Verificar que la categoría existe
        categoryRepository.findById(id)
            .orElseThrow(() -> new DomainException("Categoría con ID " + id + " no encontrada"));
        
        // Eliminar la categoría
        categoryRepository.deleteById(id);
    }
}
