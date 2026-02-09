package com.rentaherramientas.tolly.application.mapper;

import java.math.BigDecimal;

import com.rentaherramientas.tolly.application.dto.invoice.InvoiceDetailResponse;
import com.rentaherramientas.tolly.domain.model.InvoiceDetail;
import com.rentaherramientas.tolly.domain.model.Tool;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.InvoiceDetailEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.ToolEntity;

public class InvoiceDetailMapper {

  private InvoiceDetailMapper() {
  }

  public static InvoiceDetail toDomain(InvoiceDetailEntity entity) {
    if (entity == null) return null;

    return new InvoiceDetail(
        entity.getId(),
        entity.getInvoice() != null ? entity.getInvoice().getId() : null,
        toToolDomain(entity.getTool()),
        entity.getDailyPrice(),
        entity.getRentalDay(),
        entity.getQuantity() != null ? entity.getQuantity() : 1,
        entity.getSubTotal()
    );
  }

  public static InvoiceDetailEntity toEntity(InvoiceDetail domain) {
    if (domain == null) return null;

    InvoiceDetailEntity entity = new InvoiceDetailEntity();
    entity.setId(domain.getId());

    if (domain.getDailyPrice() != null) {
      entity.setDailyPrice(domain.getDailyPrice());
    } else {
      entity.setDailyPrice(BigDecimal.ZERO);
    }

    entity.setRentalDay(domain.getRentalDay());
    entity.setQuantity(domain.getQuantity());
    entity.setSubTotal(domain.getSubTotal());

    if (domain.getTool() != null) {
      ToolEntity tool = new ToolEntity();
      tool.setId(domain.getTool().getId());
      entity.setTool(tool);
    }

    return entity;
  }

  public static InvoiceDetailResponse toResponse(InvoiceDetail domain) {
    if (domain == null) return null;

    String toolName = domain.getTool() != null ? domain.getTool().getName() : null;
    Long toolId = domain.getTool() != null ? domain.getTool().getId() : null;

    return new InvoiceDetailResponse(
        toolId,
        toolName,
        domain.getDailyPrice(),
        domain.getRentalDay(),
        domain.getQuantity(),
        domain.getSubTotal()
    );
  }

  private static Tool toToolDomain(ToolEntity entity) {
    if (entity == null) return null;

    return Tool.reconstruct(
        entity.getId(),
        entity.getName(),
        entity.getDescription(),
        entity.getDailyPrice(),
        entity.getTotalQuantity(),
        entity.getAvailableQuantity(),
        entity.getToolStatus() != null ? entity.getToolStatus().getId() : null,
        entity.getSupplier() != null ? entity.getSupplier().getId() : null,
        entity.getCategory() != null ? entity.getCategory().getId() : null
    );
  }
}
