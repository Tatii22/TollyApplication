package com.rentaherramientas.tolly.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.rentaherramientas.tolly.domain.model.ReturnStatus;
import com.rentaherramientas.tolly.application.dto.returnstatus.CreateReturnStatusRequest;
import com.rentaherramientas.tolly.application.dto.returnstatus.ReturnStatusResponse;

/**
 * Mapper MapStruct para ReturnStatus
 */
@Mapper(componentModel = "spring")
public interface ReturnStatusMapper {

    ReturnStatusResponse toReturnStatusResponse(ReturnStatus returnStatus);

    @Mapping(target = "id", ignore = true)
    ReturnStatus toReturnStatus(CreateReturnStatusRequest request);
}
