package com.firestms.service;

import com.firestms.model.Car;
import com.firestms.model.CarPOJO;
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
    public List<CarPOJO> getAllCars() {
        List<Car> listOfAllCars = Lists.newArrayList(carRepository.findAll());
        return listOfAllCars.stream().map(CarMapper::mapCarToCarPOJO).collect(Collectors.toList());
    }

    @Override
    public Optional<CarPOJO> findByRegistrationNumber(String registrationNumber) {
        return carRepository.findById(registrationNumber).map(CarMapper::mapCarToCarPOJO);
    }

    @Override
    public void deleteCarById(String registrationNumber) {

    }

    @Override
    public CarPOJO addCar(CarPOJO carPOJO) {
        return CarMapper.mapCarToCarPOJO(carRepository.save(CarMapper.mapCarPOJOToCar(carPOJO)));
    }

    @Override
    public CarPOJO updateCar(CarPOJO car) {
        return null;
    }

    private class CarMapper {

        static CarPOJO mapCarToCarPOJO(Car car){
            return CarPOJO.builder()
                .registrationNumber(car.getRegistrationNumber())
                .company(car.getCompany())
                .build();
        }

        static Car mapCarPOJOToCar(CarPOJO carPOJO){
            return Car.builder()
                .registrationNumber(carPOJO.getRegistrationNumber())
                .company(carPOJO.getCompany())
                .build();
        }
    }
}
