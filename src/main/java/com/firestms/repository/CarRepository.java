package com.firestms.repository;

import com.firestms.model.CarEntity;
import org.springframework.data.repository.CrudRepository;


public interface CarRepository extends CrudRepository<CarEntity, String> {
}
