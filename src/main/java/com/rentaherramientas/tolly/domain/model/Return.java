package com.rentaherramientas.tolly.domain.model;

import java.time.LocalDate;

public class Return {

    private Long id;
    private Long reservationId;
    private Long clientId;
    private LocalDate returnDate;
    private ReturnStatus status;
    private String observations;

    public Return() {
    }

    private Return(Long id, Long reservationId, Long clientId, LocalDate returnDate, ReturnStatus status,
            String observations) {
        this.id = id;
        this.reservationId = reservationId;
        this.clientId = clientId;
        this.returnDate = returnDate;
        this.status = status;
        this.observations = observations;
    }

    public static Return create(Long reservationId, Long clientId, LocalDate returnDate, ReturnStatus status,
            String observations) {
        Return value = new Return();
        value.reservationId = reservationId;
        value.clientId = clientId;
        value.returnDate = returnDate;
        value.status = status;
        value.observations = observations;
        return value;
    }

    public static Return reconstruct(Long id, Long reservationId, Long clientId, LocalDate returnDate,
            ReturnStatus status, String observations) {
        return new Return(id, reservationId, clientId, returnDate, status, observations);
    }

    public Long getId() { return id;}
    public void setId(Long id) {this.id = id;}

    public Long getReservationId() { return reservationId;}
    public void setReservationId(Long reservationId) {this.reservationId = reservationId;}

    public Long getClientId() { return clientId;}
    public void setClientId(Long clientId) {this.clientId = clientId;}

    public LocalDate getReturnDate() { return returnDate;}
    public void setReturnDate(LocalDate returnDate) {this.returnDate = returnDate;}

    public ReturnStatus getStatus() { return status;}
    public void setStatus(ReturnStatus status) {this.status = status;}

    public String getObservations() { return observations;}
    public void setObservations(String observations) {this.observations = observations;}
}
