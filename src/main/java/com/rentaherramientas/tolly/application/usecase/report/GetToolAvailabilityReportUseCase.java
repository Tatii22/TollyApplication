package com.rentaherramientas.tolly.application.usecase.report;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.application.dto.report.ToolAvailabilityReportResponse;
import com.rentaherramientas.tolly.domain.ports.ToolRepository;
import com.rentaherramientas.tolly.domain.ports.ToolStatusRepository;

@Service
public class GetToolAvailabilityReportUseCase {

  private final ToolRepository toolRepository;
  private final ToolStatusRepository toolStatusRepository;

  public GetToolAvailabilityReportUseCase(ToolRepository toolRepository,
      ToolStatusRepository toolStatusRepository) {
    this.toolRepository = toolRepository;
    this.toolStatusRepository = toolStatusRepository;
  }

  public List<ToolAvailabilityReportResponse> execute() {
    return toolRepository.findAll().stream()
        .map(tool -> {
          String statusName = tool.getStatusId() != null
              ? toolStatusRepository.findById(tool.getStatusId()).map(s -> s.getName()).orElse(null)
              : null;
          return new ToolAvailabilityReportResponse(
              tool.getId(),
              tool.getName(),
              tool.getTotalQuantity(),
              tool.getAvailableQuantity(),
              statusName);
        })
        .toList();
  }
}
