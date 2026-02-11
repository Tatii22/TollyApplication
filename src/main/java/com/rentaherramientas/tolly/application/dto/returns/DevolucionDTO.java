package com.rentaherramientas.tolly.application.dto.returns;


public class DevolucionDTO {
  
 
  private String estado;
  private String descripcionDano;

  
  private Double costoReparacion;

  public DevolucionDTO() {
  }

  public DevolucionDTO(String estado, String descripcionDano, Double costoReparacion) {
    this.estado = estado;
    this.descripcionDano = descripcionDano;
    this.costoReparacion = costoReparacion;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public String getDescripcionDano() {
    return descripcionDano;
  }

  public void setDescripcionDano(String descripcionDano) {
    this.descripcionDano = descripcionDano;
  }

  public Double getCostoReparacion() {
    return costoReparacion;
  }

  public void setCostoReparacion(Double costoReparacion) {
    this.costoReparacion = costoReparacion;
  }
}
