package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "returns")
public class ReturnEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "returns_id")
    private Long id;

    @Column(name = "reservation_id", nullable = false)
    private Long reservationId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id", nullable = false)
    private ClientEntity clientId;

    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private ReturnStatusEntity status;

    @Column(name = "observations", length = 255)
    private String observations;

    // Constructor vacío requerido por JPA
    public ReturnEntity() {
    }

    // Constructor completo
    public ReturnEntity(Long id,
                        Long reservationId,
                        ClientEntity clientId,
                        LocalDate returnDate,
                        ReturnStatusEntity status,
                        String observations) {
        this.id = id;
        this.reservationId = reservationId;
        this.clientId = clientId;
        this.returnDate = returnDate;
        this.status = status;
        this.observations = observations;
    }

    // Constructor sin ID
    public ReturnEntity(Long reservationId,
                        ClientEntity clientId,
                        LocalDate returnDate,
                        ReturnStatusEntity status,
                        String observations) {
        this.reservationId = reservationId;
        this.clientId = clientId;
        this.returnDate = returnDate;
        this.status = status;
        this.observations = observations;
    }

    // Getters y Setters

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

    public ClientEntity getClientId() {
        return clientId;
    }

    public void setClientId(ClientEntity clientId) {
        this.clientId = clientId;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public ReturnStatusEntity getStatus() {
        return status;
    }

    public void setStatus(ReturnStatusEntity status) {
        this.status = status;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}
