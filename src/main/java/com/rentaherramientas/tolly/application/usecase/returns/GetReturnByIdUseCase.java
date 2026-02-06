package com.rentaherramientas.tolly.application.usecase.returns;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.application.dto.returns.ReturnResponse;
import com.rentaherramientas.tolly.application.mapper.ReturnMapper;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Return;
import com.rentaherramientas.tolly.domain.ports.ReturnRepository;

@Service
public class GetReturnByIdUseCase {

    private final ReturnRepository returnRepository;
    private final ReturnMapper returnMapper;

    public GetReturnByIdUseCase(ReturnRepository returnRepository, ReturnMapper returnMapper) {
        this.returnRepository = returnRepository;
        this.returnMapper = returnMapper;
    }

    public ReturnResponse execute(Long id) {
        Return value = returnRepository.findById(id)
            .orElseThrow(() -> new DomainException("Devolucion no encontrada con ID: " + id));

        return returnMapper.toReturnResponse(value);
    }
}
