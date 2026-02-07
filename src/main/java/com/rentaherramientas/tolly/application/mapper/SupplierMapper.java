package com.rentaherramientas.tolly.application.mapper;

import org.springframework.stereotype.Component;

import com.rentaherramientas.tolly.domain.model.Supplier;
import com.rentaherramientas.tolly.domain.model.User;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.SupplierEntity;
import com.rentaherramientas.tolly.infrastructure.persistence.entity.UserEntity;

@Component
public class SupplierMapper {

  private SupplierMapper(){

  }

  public SupplierEntity toEntity(Supplier supplier, UserEntity userEntity){
    SupplierEntity entity = new SupplierEntity();
    entity.setId(supplier.getId());
    entity.setUserId(userEntity);
    entity.setPhone(supplier.getPhone());
    entity.setCompanyName(supplier.getCompany());
    entity.setIdentification(supplier.getIdentification());
    entity.setContactName(supplier.getContactName());
    return entity;
  }

  public Supplier toDomain(SupplierEntity entity){
    return Supplier.restore(
      entity.getId(),
      User.restore(entity.getUserId().getId()),
      entity.getPhone(),
      entity.getCompanyName(),
      entity.getIdentification(),
      entity.getContactName()
    );
  }
}
