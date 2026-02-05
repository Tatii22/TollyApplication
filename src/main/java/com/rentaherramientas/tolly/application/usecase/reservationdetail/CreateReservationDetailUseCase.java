package com.rentaherramientas.tolly.application.usecase.reservationdetail;

import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rentaherramientas.tolly.domain.model.Reservation;
import com.rentaherramientas.tolly.domain.model.ReservationDetail;
import com.rentaherramientas.tolly.domain.model.Tool;
import com.rentaherramientas.tolly.domain.ports.ReservationDetailRepository;
import com.rentaherramientas.tolly.domain.ports.ReservationRepository;
import com.rentaherramientas.tolly.domain.ports.ToolRepository;

@Service
@Transactional
public class CreateReservationDetailUseCase {

  private final ReservationDetailRepository reservationDetailRepository;
  private final ReservationRepository reservationRepository;
  private final ToolRepository toolRepository;

  public CreateReservationDetailUseCase(
      ReservationDetailRepository reservationDetailRepository,
      ReservationRepository reservationRepository,
      ToolRepository toolRepository) {

    this.reservationDetailRepository = reservationDetailRepository;
    this.reservationRepository = reservationRepository;
    this.toolRepository = toolRepository;
  }

  public ReservationDetail execute(Long reservationId, Long toolId) {

    // 1️⃣ Obtener la reserva 🧾
    Reservation reservation = reservationRepository.findById(reservationId)
        .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

    // 2️⃣ Obtener la herramienta 🛠️
    Tool tool = toolRepository.findById(toolId)
        .orElseThrow(() -> new RuntimeException("Herramienta no encontrada"));

    // 3️⃣ Calcular días de alquiler 📆
    long rentalDays = ChronoUnit.DAYS.between(
        reservation.getStartDate(),
        reservation.getEndDate());

    if (rentalDays <= 0) {
      throw new RuntimeException("La fecha de fin debe ser mayor a la fecha de inicio");
    }

    if (rentalDays > Integer.MAX_VALUE) {
      throw new RuntimeException("El número de días es demasiado grande");
    }

    int rentalDaysInt = (int) rentalDays;

    // 4️⃣ Obtener precio diario como Double 💰
    Double dailyPrice = tool.getDailyPrice();

    // 5️⃣ Crear detalle (EL DOMINIO CALCULA EL SUBTOTAL) 🧩
    ReservationDetail detail = ReservationDetail.create(
        tool,
        reservation,
        dailyPrice,
        rentalDaysInt,
        null // ⚠️ este valor NO se usa, el dominio recalcula
    );

    // 6️⃣ Guardar 💾
    return reservationDetailRepository.save(detail);
  }
}
