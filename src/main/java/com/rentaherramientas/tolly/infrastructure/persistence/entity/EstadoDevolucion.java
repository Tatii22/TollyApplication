package com.rentaherramientas.tolly.infrastructure.persistence.entity;

/**
 * Enum para representar los estados de devolución de una reserva.
 */
public enum EstadoDevolucion {
  /** Estado inicial cuando la reserva está en curso */
  PENDIENTE_DEVOLUCION,
  /** Herramientas devueltas sin daños */
  DEVUELTO_OK,
  /** Herramientas devueltas con daños */
  DEVUELTO_CON_DAÑOS
}
