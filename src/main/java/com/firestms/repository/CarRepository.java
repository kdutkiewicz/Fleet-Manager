package com.firestms.repository;

import com.firestms.model.Car;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends CrudRepository<Car, String> {

    public Optional<Car> findByRegistrationNumber(String registrationNumber);
}
