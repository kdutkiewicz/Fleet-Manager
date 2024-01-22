package com.firestms.service;

import com.firestms.model.Car;
import com.firestms.model.CarEntity;

import java.util.List;
import java.util.Optional;

public interface CarService {

    List<Car> getAllCars();

    Optional<Car> findByRegistrationNumber(String registrationNumber);

    void deleteCarById(String registrationNumber);

    CarEntity addCar(CarEntity carPOJO);

    CarEntity updateCar(CarEntity car);

}
