package com.rentaherramientas.tolly.application.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.rentaherramientas.tolly.domain.model.ToolStatus;
import com.rentaherramientas.tolly.application.dto.tool.CreateToolStatusRequest;
import com.rentaherramientas.tolly.application.dto.tool.ToolStatusResponse;

/**
 * Mapper MapStruct para ToolStatus
 */
@Mapper(componentModel = "spring")
public interface ToolStatusMapper {
    
    ToolStatusResponse toToolStatusResponse(ToolStatus toolStatus);
    
    @Mapping(target = "id", ignore = true)
    ToolStatus toToolStatus(CreateToolStatusRequest request);
}
