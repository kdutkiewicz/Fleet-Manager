package com.firestms.service;

import com.firestms.model.CarPOJO;

import java.util.List;
import java.util.Optional;

public interface CarService {

    List<CarPOJO> getAllCars();

    Optional<CarPOJO> findByRegistrationNumber(String registrationNumber);

    void deleteCarById(String registrationNumber);

    CarPOJO addCar(CarPOJO carPOJO);

    CarPOJO updateCar(CarPOJO car);

}
