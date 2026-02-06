package com.rentaherramientas.tolly.application.usecase.reservationdetail;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.model.Tool;
import com.rentaherramientas.tolly.domain.ports.ToolRepository;

@Service
@Transactional(readOnly = true)
public class GetAvailableToolsUseCase {

  private final ToolRepository toolRepository;

  public GetAvailableToolsUseCase(ToolRepository toolRepository) {
    this.toolRepository = toolRepository;
  }

  public List<Tool> execute() {
    return toolRepository.findByAvailableQuantityGreaterThan(0);
  }
}
