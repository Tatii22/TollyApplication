package com.rentaherramientas.tolly.domain.ports;

import com.rentaherramientas.tolly.domain.model.Supplier;

public interface SupplierRepository {
  void save(Supplier supplier);
}
