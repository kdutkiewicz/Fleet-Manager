package com.firestms.repository;

import com.firestms.model.Car;
import org.springframework.data.repository.CrudRepository;


public interface CarRepository extends CrudRepository<Car, String> {
}
