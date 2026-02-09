package com.rentaherramientas.tolly.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import com.rentaherramientas.tolly.application.dto.returns.CreateReturnRequest;
import com.rentaherramientas.tolly.application.dto.returns.ReturnDetailResponse;
import com.rentaherramientas.tolly.application.dto.returns.ReturnResponse;
import com.rentaherramientas.tolly.application.dto.returns.UpdateReturnRequest;
import com.rentaherramientas.tolly.domain.model.Return;
import com.rentaherramientas.tolly.domain.model.ReturnStatus;

@Mapper(componentModel = "spring")
public interface ReturnMapper {

    @Mapping(target = "reservationId", source = "reservationId")
    @Mapping(target = "clientId", source = "clientId")
    @Mapping(target = "returnStatusId", source = "status.id")
    @Mapping(target = "returnStatusName", source = "status.name")
    @Mapping(target = "details", ignore = true)
    ReturnResponse toReturnResponse(Return domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reservationId", source = "reservationId")
    @Mapping(target = "status", source = "returnStatusId")
    Return toReturn(CreateReturnRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reservationId", source = "reservationId")
    @Mapping(target = "clientId", source = "clientId")
    @Mapping(target = "status", source = "returnStatusId")
    Return toReturn(UpdateReturnRequest request);

    default ReturnStatus mapReturnStatus(Long returnStatusId) {
        return returnStatusId != null ? new ReturnStatus(returnStatusId, null) : null;
    }

    default ReturnResponse toReturnResponse(Return domain, List<ReturnDetailResponse> details) {
        ReturnResponse base = toReturnResponse(domain);
        return new ReturnResponse(
            base.id(),
            base.reservationId(),
            base.clientId(),
            base.returnDate(),
            base.returnStatusId(),
            base.returnStatusName(),
            base.observations(),
            details
        );
    }
}
