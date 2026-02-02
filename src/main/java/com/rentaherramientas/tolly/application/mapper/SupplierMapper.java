package com.rentaherramientas.tolly.application.mapper;

import com.rentaherramientas.tolly.domain.model.Supplier;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.SupplierEntity;

public class SupplierMapper {

  private SupplierMapper(){

  }

  public static SupplierEntity toEntity(Supplier supplier){
    SupplierEntity entity = new SupplierEntity();
    entity.setId(supplier.getId());
    entity.setUserId(supplier.getUserId());
    entity.setPhone(supplier.getPhone());
    entity.setCompany(supplier.getCompany());
    return entity;
  }

  public static Supplier toDomain(SupplierEntity entity){
    return Supplier.restore(
      entity.getId(),
      entity.getUserId(),
      entity.getPhone(),
      entity.getCompany()
    );
  }
}
