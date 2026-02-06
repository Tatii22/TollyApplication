package com.rentaherramientas.tolly.application.usecase.returnstatus;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.rentaherramientas.tolly.domain.ports.ReturnStatusRepository;
import com.rentaherramientas.tolly.domain.model.ReturnStatus;
import com.rentaherramientas.tolly.application.mapper.ReturnStatusMapper;
import com.rentaherramientas.tolly.application.dto.returnstatus.CreateReturnStatusRequest;
import com.rentaherramientas.tolly.application.dto.returnstatus.ReturnStatusResponse;

/**
 * UseCase para crear un nuevo estado de devolucion
 * Solo los ADMIN pueden crear estados globales del sistema
 */
@Service
public class CreateReturnStatusUseCase {
    private final ReturnStatusRepository returnStatusRepository;
    private final ReturnStatusMapper returnStatusMapper;

    public CreateReturnStatusUseCase(ReturnStatusRepository returnStatusRepository, ReturnStatusMapper returnStatusMapper) {
        this.returnStatusRepository = returnStatusRepository;
        this.returnStatusMapper = returnStatusMapper;
    }

    @Transactional
    public ReturnStatusResponse execute(CreateReturnStatusRequest request) {
        ReturnStatus returnStatus = returnStatusMapper.toReturnStatus(request);
        ReturnStatus saved = returnStatusRepository.save(returnStatus);

        return returnStatusMapper.toReturnStatusResponse(saved);
    }
}
