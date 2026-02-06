package com.rentaherramientas.tolly.application.usecase.returns;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.application.dto.returns.ReturnResponse;
import com.rentaherramientas.tolly.application.mapper.ReturnMapper;
import com.rentaherramientas.tolly.domain.ports.ReturnRepository;

@Service
public class GetReturnsUseCase {

    private final ReturnRepository returnRepository;
    private final ReturnMapper returnMapper;

    public GetReturnsUseCase(ReturnRepository returnRepository, ReturnMapper returnMapper) {
        this.returnRepository = returnRepository;
        this.returnMapper = returnMapper;
    }

    public List<ReturnResponse> execute() {
        return returnRepository.findAll()
            .stream()
            .map(returnMapper::toReturnResponse)
            .toList();
    }
}
