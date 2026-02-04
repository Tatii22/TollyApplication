package com.rentaherramientas.tolly.domain.ports;

import java.util.Optional;

import com.rentaherramientas.tolly.domain.model.Supplier;
import com.rentaherramientas.tolly.domain.model.User;

public interface SupplierRepository {
  void save(Supplier supplier);
  Optional<Supplier> findByUserId(User userId);
  void delete(Supplier supplier);
}
