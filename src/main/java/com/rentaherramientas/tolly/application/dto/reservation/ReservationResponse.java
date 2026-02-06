package com.rentaherramientas.tolly.application.dto.reservation;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Respuesta de reserva creada")
public record ReservationResponse(
    @Schema(description = "ID de la reserva", example = "1")
    Long id,

    @Schema(description = "ID del cliente", example = "1")
    Long clientId,

    @Schema(description = "Fecha de inicio", example = "2026-02-05")
    LocalDate startDate,

    @Schema(description = "Fecha de fin", example = "2026-02-10")
    LocalDate endDate,

    @Schema(description = "Precio total", example = "150000.00")
    BigDecimal total,

    @Schema(description = "Estado de la reserva", example = "IN_PROGRESS")
    String statusName,

    @Schema(description = "Fecha de creación", example = "2026-02-05")
    LocalDate createdAt
) {

}
