package com.rentaherramientas.tolly.domain.ports;

import java.util.Optional;
import java.util.UUID;

import com.rentaherramientas.tolly.domain.model.Supplier;

public interface SupplierRepository {

  void save(Supplier supplier);

  Optional<Supplier> findByUserId(UUID userId);

  Optional<Supplier> findById(Long id);

  void delete(Supplier supplier);
}
