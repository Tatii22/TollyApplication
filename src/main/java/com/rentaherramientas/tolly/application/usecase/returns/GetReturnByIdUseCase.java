package com.rentaherramientas.tolly.application.usecase.returns;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.application.dto.returns.ReturnDetailResponse;
import com.rentaherramientas.tolly.application.dto.returns.ReturnResponse;
import com.rentaherramientas.tolly.application.mapper.ReturnDetailMapper;
import com.rentaherramientas.tolly.application.mapper.ReturnMapper;
import com.rentaherramientas.tolly.domain.exceptions.DomainException;
import com.rentaherramientas.tolly.domain.model.Return;
import com.rentaherramientas.tolly.domain.ports.ReturnDetailRepository;
import com.rentaherramientas.tolly.domain.ports.ReturnRepository;

@Service
public class GetReturnByIdUseCase {

    private final ReturnRepository returnRepository;
    private final ReturnDetailRepository returnDetailRepository;
    private final ReturnMapper returnMapper;

    public GetReturnByIdUseCase(
        ReturnRepository returnRepository,
        ReturnDetailRepository returnDetailRepository,
        ReturnMapper returnMapper) {
        this.returnRepository = returnRepository;
        this.returnDetailRepository = returnDetailRepository;
        this.returnMapper = returnMapper;
    }

    public ReturnResponse execute(Long id) {
        Return value = returnRepository.findById(id)
            .orElseThrow(() -> new DomainException("Devolucion no encontrada con ID: " + id));

        java.util.List<ReturnDetailResponse> details = returnDetailRepository.findByReturnId(id)
            .stream()
            .map(ReturnDetailMapper::toResponse)
            .toList();

        return returnMapper.toReturnResponse(value, details);
    }
}
