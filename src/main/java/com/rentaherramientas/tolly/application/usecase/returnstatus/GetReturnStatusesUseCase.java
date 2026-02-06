package com.rentaherramientas.tolly.application.usecase.returnstatus;

import org.springframework.stereotype.Service;
import com.rentaherramientas.tolly.domain.ports.ReturnStatusRepository;
import com.rentaherramientas.tolly.application.mapper.ReturnStatusMapper;
import com.rentaherramientas.tolly.application.dto.returnstatus.ReturnStatusResponse;
import java.util.List;

/**
 * UseCase para obtener todos los estados de devolucion disponibles
 */
@Service
public class GetReturnStatusesUseCase {
    private final ReturnStatusRepository returnStatusRepository;
    private final ReturnStatusMapper returnStatusMapper;

    public GetReturnStatusesUseCase(ReturnStatusRepository returnStatusRepository, ReturnStatusMapper returnStatusMapper) {
        this.returnStatusRepository = returnStatusRepository;
        this.returnStatusMapper = returnStatusMapper;
    }

    public List<ReturnStatusResponse> execute() {
        return returnStatusRepository.findAll()
            .stream()
            .map(returnStatusMapper::toReturnStatusResponse)
            .toList();
    }
}
