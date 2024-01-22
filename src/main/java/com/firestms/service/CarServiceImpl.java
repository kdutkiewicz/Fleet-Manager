package com.firestms.service;

import com.firestms.model.CarEntity;
import com.firestms.model.Car;
import com.firestms.repository.CarRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    CarRepository carRepository;

    @Override
    public List<Car> getAllCars() {
        List<CarEntity> listOfAllCars = Lists.newArrayList(carRepository.findAll());
        return listOfAllCars.stream().map(CarMapper::mapCarToCarPOJO).collect(Collectors.toList());
    }

    @Override
    public Optional<Car> findByRegistrationNumber(String registrationNumber) {
        return carRepository.findById(registrationNumber).map(CarMapper::mapCarToCarPOJO);
    }

    @Override
    public void deleteCarById(String registrationNumber) {

    }

    @Override
    public CarEntity addCar(CarEntity car) {
        return carRepository.save(car);
    }

    @Override
    public CarEntity updateCar(CarEntity car) {
        return null;
    }

    private class CarMapper {

        static Car mapCarToCarPOJO(CarEntity car){
            return Car.builder()
                .registrationNumber(car.getRegistrationNumber())
                .build();
        }

        static CarEntity mapCarPOJOToCar(Car carPOJO){
            return CarEntity.builder()
                .registrationNumber(carPOJO.getRegistrationNumber())
                .build();
        }
    }
}
