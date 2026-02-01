package com.rentaherramientas.tolly.application.mapper;

import com.rentaherramientas.tolly.domain.model.enums.ToolStatus;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ToolMapper{

    // Convierte ToolStatus a String (almacenado en la base de datos)
    default String toolStatusToString(ToolStatus status) {
        return status != null ? status.name() : null;  // Usa el nombre del enum como String
    }

    // Convierte String (de la base de datos) a ToolStatus (enum)
    default ToolStatus stringToToolStatus(String status) {
        return status != null ? ToolStatus.valueOf(status) : null;
    }
}

