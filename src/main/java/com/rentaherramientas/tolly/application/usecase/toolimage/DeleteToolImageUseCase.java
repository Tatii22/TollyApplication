package com.rentaherramientas.tolly.application.usecase.toolimage;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.ports.ToolImageRepository;

/**
 * UseCase para eliminar imagen por ID
 */
@Service
public class DeleteToolImageUseCase {

    private final ToolImageRepository toolImageRepository;

    public DeleteToolImageUseCase(ToolImageRepository toolImageRepository) {
        this.toolImageRepository = toolImageRepository;
    }

    @Transactional
    public void execute(Long id) {
        toolImageRepository.findById(id)
            .orElseThrow(() -> new DomainException("Imagen con ID " + id + " no encontrada"));
        toolImageRepository.deleteById(id);
    }
}
