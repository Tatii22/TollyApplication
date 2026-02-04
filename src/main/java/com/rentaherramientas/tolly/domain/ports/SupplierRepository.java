package com.rentaherramientas.tolly.domain.ports;

import java.util.Optional;

import com.rentaherramientas.tolly.domain.model.Supplier;
import com.rentaherramientas.tolly.domain.model.User;

public interface SupplierRepository {

  void save(Supplier supplier);

  Optional<Supplier> findByUserId(UUID userId);

  Optional<Supplier> findById(UUID id);

  void delete(Supplier supplier);
}