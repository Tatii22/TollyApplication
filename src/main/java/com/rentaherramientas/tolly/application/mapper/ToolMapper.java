package com.rentaherramientas.tolly.application.mapper;

import com.rentaherramientas.tolly.application.dto.tool.CreateToolRequest;
import com.rentaherramientas.tolly.application.dto.tool.ToolResponse;
import com.rentaherramientas.tolly.application.dto.tool.UpdateToolRequest;
import com.rentaherramientas.tolly.domain.model.Tool;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper MapStruct para conversión entre Tool (dominio) y DTOs
 */
@Mapper(componentModel = "spring")
public interface ToolMapper {

    /**
     * Convierte Tool de dominio a ToolResponse (DTO)
     */
    ToolResponse toToolResponse(Tool tool);

    /**
     * Convierte CreateToolRequest (DTO) a Tool de dominio
     */
    @Mapping(target = "id", ignore = true)
    Tool toTool(CreateToolRequest request);

    /**
     * Convierte UpdateToolRequest (DTO) a Tool de dominio
     */
    @Mapping(target = "id", ignore = true)
    Tool toTool(UpdateToolRequest request);
}

