package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_payment")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_reservation", nullable = false)
    private ReservationEntity reservation;
    
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column     (name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;      

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_payment_status", nullable = false)
    private PaymentStatusEntity status;

    public PaymentEntity() {
    }

    public PaymentEntity(Long id, ReservationEntity reservation, BigDecimal amount, LocalDateTime paymentDate,
            PaymentStatusEntity status) {
        this.id = id;
        this.reservation = reservation;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public ReservationEntity getReservation() {
        return reservation;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public PaymentStatusEntity getStatus() {
        return status;
    }
        

}
