package com.rentaherramientas.tolly.infrastructure.persistence.entity;

import java.time.LocalDateTime;

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
@Table(name = "ReporteDanos")
public class ReporteDano {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_reported")
  private Long id;
  @Column(name = "descripcion", columnDefinition = "TEXT")
  private String descripcion;
  @Column(name = "costo_reparacion", nullable = false, precision = 10, scale = 2)
  private Double costoReparacion;
  @Column(name = "fecha_reporte")
  private LocalDateTime fechaReporte;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "id_reservation", nullable = false)
  private ReservationEntity reservation;


  public ReporteDano() {
  }


  public Long getId() {
    return id;
  }


  public String getDescripcion() {
    return descripcion;
  }


  public Double getCostoReparacion() {
    return costoReparacion;
  }


  public LocalDateTime getFechaReporte() {
    return fechaReporte;
  }


  public ReservationEntity getReservation() {
    return reservation;
  }
  
}
