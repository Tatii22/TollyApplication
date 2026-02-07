package com.rentaherramientas.tolly.application.usecase.report;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rentaherramientas.tolly.application.dto.report.FrequentClientReportResponse;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;

@Service
public class GetFrequentClientsReportUseCase {

  private final ReservationRepository reservationRepository;

  public GetFrequentClientsReportUseCase(ReservationRepository reservationRepository) {
    this.reservationRepository = reservationRepository;
  }

  public List<FrequentClientReportResponse> execute(LocalDate from, LocalDate to, int limit) {
    List<Object[]> rows = reservationRepository.findFrequentClients(from, to);
    return rows.stream()
        .limit(limit)
        .map(row -> {
          Long clientId = (Long) row[0];
          String firstName = (String) row[1];
          String lastName = (String) row[2];
          Long count = ((Number) row[3]).longValue();
          String fullName = (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
          return new FrequentClientReportResponse(clientId, fullName.trim(), count);
        })
        .toList();
  }
}
