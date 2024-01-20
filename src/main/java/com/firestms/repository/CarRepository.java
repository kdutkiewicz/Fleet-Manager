package com.firestms.repository;

import com.firestms.model.Car;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;


public interface CarRepository extends CrudRepository<Car, String> {
}
