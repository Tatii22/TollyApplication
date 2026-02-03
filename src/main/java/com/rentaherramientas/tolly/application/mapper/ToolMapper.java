package com.rentaherramientas.tolly.application.mapper;

import com.rentaherramientas.tolly.application.dto.tool.CreateToolRequest;
import com.rentaherramientas.tolly.application.dto.tool.ToolResponse;
import com.rentaherramientas.tolly.application.dto.tool.UpdateToolRequest;
import com.rentaherramientas.tolly.domain.model.Tool;
import com.rentaherramientas.tolly.domain.model.enums.ToolStatus;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;


/**
 * Mapper MapStruct para conversión entre Tool (dominio) y DTOs
 */
@Mapper(componentModel = "spring")
public interface ToolMapper {

    /**
     * Convierte Tool de dominio a ToolResponse (DTO)
     */
    @Mapping(target = "status", expression = "java(toolStatusToString(tool.getStatus()))")
    ToolResponse toToolResponse(Tool tool);

    /**
     * Convierte CreateToolRequest (DTO) a Tool de dominio
     * El status se asigna automáticamente a AVAIBLE (disponible)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(com.rentaherramientas.tolly.domain.model.enums.ToolStatus.AVAIBLE)")
    @Mapping(target = "stock", source = "stock")
    Tool toTool(CreateToolRequest request);

    /**
     * Convierte UpdateToolRequest (DTO) a Tool de dominio
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    Tool toTool(UpdateToolRequest request);

    /**
     * Convierte ToolStatus enum a String (para JSON)
     */
    default String toolStatusToString(ToolStatus status) {
        return status != null ? status.name() : null;
    }

    /**
     * Convierte String a ToolStatus enum (desde JSON)
     */
    default ToolStatus stringToToolStatus(String status) {
        return status != null ? ToolStatus.valueOf(status) : null;
    }
}

