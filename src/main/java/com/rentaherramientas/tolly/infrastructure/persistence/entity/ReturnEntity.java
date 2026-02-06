package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "returns")
public class ReturnEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_return")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_reservation", nullable = false)
    private ReservationEntity reservation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_client", nullable = false)
    private ClientEntity client;

    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_return_status", nullable = false)
    private ReturnStatusEntity returnStatus;

    @Column(name = "observations", columnDefinition = "TEXT")
    private String observations;

    public ReturnEntity() {}

    public Long getId() { return id;}
    public void setId(Long id) {this.id = id;}

    public ReservationEntity getReservation() { return reservation;}
    public void setReservation(ReservationEntity reservation) {this.reservation = reservation;}

    public Long getReservationId() { return reservation != null ? reservation.getId() : null;}
    public void setReservationId(Long reservationId) {
        if (reservation == null) reservation = new ReservationEntity();
        reservation.setId(reservationId);
    }

    public ClientEntity getClient() { return client;}
    public void setClient(ClientEntity client) {this.client = client;}

    public Long getClientId() { return client != null ? client.getId() : null;}
    public void setClientId(Long clientId) {
        if (client == null) client = new ClientEntity();
        client.setId(clientId);
    }

    public LocalDate getReturnDate() { return returnDate;}
    public void setReturnDate(LocalDate returnDate) {this.returnDate = returnDate;}

    public ReturnStatusEntity getReturnStatus() { return returnStatus;}
    public void setReturnStatus(ReturnStatusEntity returnStatus) {this.returnStatus = returnStatus;}

    public Long getReturnStatusId() { return returnStatus != null ? returnStatus.getId() : null;}
    public void setReturnStatusId(Long returnStatusId) {
        if (returnStatus == null) returnStatus = new ReturnStatusEntity();
        returnStatus.setId(returnStatusId);
    }

    public String getObservations() { return observations;}
    public void setObservations(String observations) {this.observations = observations;}
}
