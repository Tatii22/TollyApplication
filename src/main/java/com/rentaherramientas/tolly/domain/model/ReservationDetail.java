package com.rentaherramientas.tolly.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class ReservationDetail {

  private Long id;
  private Tool tool;
  private Reservation reservation;

  private Double dailyPrice;
  private int rentalDay;
  private BigDecimal subTotal;

  /* =======================
     CONSTRUCTORES
     ======================= */

  private ReservationDetail() {
  }

  private ReservationDetail(
      Long id,
      Tool tool,
      Reservation reservation,
      Double dailyPrice,
      int rentalDay,
      BigDecimal subTotal
  ) {
    this.id = id;
    this.tool = tool;
    this.reservation = reservation;
    this.dailyPrice = dailyPrice;
    this.rentalDay = rentalDay;
    this.subTotal = subTotal;
  }

  /* =======================
     MÉTODOS DE FÁBRICA
     ======================= */

  public static ReservationDetail create(
      Tool tool,
      Reservation reservation,
      Double dailyPrice,
      int rentalDay
  ) {
    validateTool(tool);
    validateReservation(reservation);
    validateDailyPrice(dailyPrice);
    validateRentalDay(rentalDay);

    BigDecimal subTotal =
        BigDecimal.valueOf(dailyPrice)
            .multiply(BigDecimal.valueOf(rentalDay));

    return new ReservationDetail(
        null,
        tool,
        reservation,
        dailyPrice,
        rentalDay,
        subTotal
    );
  }

  public static ReservationDetail reconstruct(
      Long id,
      Tool tool,
      Reservation reservation,
      Double dailyPrice,
      int rentalDay,
      BigDecimal subTotal
  ) {
    validateTool(tool);
    validateReservation(reservation);
    validateDailyPrice(dailyPrice);
    validateRentalDay(rentalDay);

    return new ReservationDetail(
        id,
        tool,
        reservation,
        dailyPrice,
        rentalDay,
        subTotal
    );
  }

  /* =======================
     VALIDACIONES
     ======================= */

  private static void validateTool(Tool tool) {
    if (tool == null) {
      throw new IllegalArgumentException("La herramienta es obligatoria");
    }
  }

  private static void validateReservation(Reservation reservation) {
    if (reservation == null) {
      throw new IllegalArgumentException("La reserva es obligatoria");
    }
  }

  private static void validateDailyPrice(Double dailyPrice) {
    if (dailyPrice == null || dailyPrice <= 0) {
      throw new IllegalArgumentException("El precio diario debe ser mayor a 0");
    }
  }

  private static void validateRentalDay(int rentalDay) {
    if (rentalDay < 1) {
      throw new IllegalArgumentException("Los días de alquiler no pueden ser menores a 1");
    }
  }

  /* =======================
     GETTERS
     ======================= */

  public Long getId() {
    return id;
  }

  public Tool getTool() {
    return tool;
  }

  public Reservation getReservation() {
    return reservation;
  }

  public Double getDailyPrice() {
    return dailyPrice;
  }

  public int getRentalDay() {
    return rentalDay;
  }

  public BigDecimal getSubTotal() {
    return subTotal;
  }

  /* =======================
     EQUALS & HASHCODE
     ======================= */

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ReservationDetail)) return false;
    ReservationDetail that = (ReservationDetail) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
