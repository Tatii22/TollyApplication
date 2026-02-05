package com.rentaherramientas.tolly.domain.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Return {

    private Long id;
    private Long reservationId;
    private UUID clientId;
    private LocalDate returnDate;
    private ReturnStatus status;
    private String observations;

    // ✅ Constructor vacío (requerido)
    public Return() {
    }

    // ✅ Constructor sin ID (para creación)
    public Return(Long reservationId,
                  UUID clientId,
                  LocalDate returnDate,
                  ReturnStatus status,
                  String observations) {

        this.reservationId = reservationId;
        this.clientId = clientId;
        this.returnDate = returnDate;
        this.status = status;
        this.observations = observations;
        validate();
    }

    // ✅ Constructor completo (para reconstrucción desde BD)
    public Return(Long id,
                  Long reservationId,
                  UUID clientId,
                  LocalDate returnDate,
                  ReturnStatus status,
                  String observations) {

        this.id = id;
        this.reservationId = reservationId;
        this.clientId = clientId;
        this.returnDate = returnDate;
        this.status = status;
        this.observations = observations;
        validate();
    }

    // 🔹 Método de creación
    public static Return create(Long reservationId,
                                UUID clientId,
                                LocalDate returnDate,
                                ReturnStatus status,
                                String observations) {

        return new Return(reservationId, clientId, returnDate, status, observations);
    }

    // 🔹 Validaciones de dominio
    private void validate() {
        if (reservationId == null) {
            throw new IllegalArgumentException("Reservation ID is required");
        }
        if (clientId == null) {
            throw new IllegalArgumentException("Client ID is required");
        }
        if (returnDate == null) {
            throw new IllegalArgumentException("Return date is required");
        }
        if (status == null) {
            throw new IllegalArgumentException("Return status is required");
        }
    }

    // 🔹 Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public ReturnStatus getStatus() {
        return status;
    }

    public void setStatus(ReturnStatus status) {
        this.status = status;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    // 🔹 equals y hashCode (por ID)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Return)) return false;
        Return that = (Return) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
