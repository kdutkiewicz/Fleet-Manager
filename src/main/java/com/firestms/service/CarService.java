package com.firestms.service;

import com.firestms.model.Assignment;
import com.firestms.model.Car;
import com.firestms.model.CarWithAssignments;
import com.firestms.repository.AssignmentRepository;
import com.firestms.repository.CarRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarService {

    public CarService(CarRepository carRepository, AssignmentRepository assignmentRepository) {
        this.carRepository = carRepository;
        this.assignmentRepository = assignmentRepository;
    }

    CarRepository carRepository;
    AssignmentRepository assignmentRepository;

    public List<CarWithAssignments> getAllCars() {
        return Lists.newArrayList(carRepository.findAll()).stream().map(
            car ->
                CarWithAssignments.builder()
                    .assignments(assignmentRepository.findAllByCarRegistrationNumber(car.getRegistrationNumber()))
                    .registrationNumber(car.getRegistrationNumber())
                    .build()

        ).collect(Collectors.toList());
    }

    public Optional<CarWithAssignments> findByRegistrationNumber(String registrationNumber) {
        List<Assignment> assignments = assignmentRepository.findAllByCarRegistrationNumber(registrationNumber);
        return carRepository.findById(registrationNumber).map(car -> CarWithAssignments.builder()
            .assignments(assignments)
            .registrationNumber(registrationNumber)
            .build());
    }

    public void deleteCarById(String registrationNumber) {

    }

    public Car addCar(Car car) {
        return carRepository.save(car);
    }

}
