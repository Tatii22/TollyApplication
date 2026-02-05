package com.rentaherramientas.tolly.application.mapper;

import org.mapstruct.Mapper;

import com.rentaherramientas.tolly.application.dto.tool.CreateToolImageRequest;
import com.rentaherramientas.tolly.application.dto.tool.ToolImageResponse;
import com.rentaherramientas.tolly.domain.model.ToolImage;

@Mapper(componentModel = "spring")
public interface ToolImageMapper {
    ToolImageResponse toResponse(ToolImage toolImage);

    ToolImage toDomain(CreateToolImageRequest request);
}
