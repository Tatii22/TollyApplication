package com.rentaherramientas.tolly.domain.ports;

import java.util.Optional;
import java.util.List;
import com.rentaherramientas.tolly.domain.model.ReturnStatus;

public interface ReturnStatusRepository {
	Optional<ReturnStatus> findById(Long id);

	Optional<ReturnStatus> findByName(String name);

	ReturnStatus save(ReturnStatus returnStatus);

	List<ReturnStatus> findAll();
}
