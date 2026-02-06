package com.rentaherramientas.tolly.application.usecase.returns;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.ports.ReturnRepository;

@Service
public class DeleteReturnUseCase {

    private final ReturnRepository returnRepository;

    public DeleteReturnUseCase(ReturnRepository returnRepository) {
        this.returnRepository = returnRepository;
    }

    @Transactional
    public void execute(Long id) {
        returnRepository.findById(id)
            .orElseThrow(() -> new DomainException("Devolucion no encontrada con ID: " + id));

        returnRepository.deleteById(id);
    }
}
