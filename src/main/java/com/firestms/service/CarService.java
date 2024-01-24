package com.firestms.service;

import com.firestms.Exception.ConflictException;
import com.firestms.Exception.ResourceNotFoundException;
import com.firestms.model.Assignment;
import com.firestms.model.Car;
import com.firestms.model.CarWithAssignments;
import com.firestms.model.Trailer;
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

    public CarWithAssignments findByRegistrationNumber(String registrationNumber) {
        List<Assignment> assignments = assignmentRepository.findAllByCarRegistrationNumber(registrationNumber);
        return CarWithAssignments.builder()
            .assignments(assignments)
            .registrationNumber(findCarByRegistrationNumber(registrationNumber).getRegistrationNumber())
            .build();
    }

    public Car findCarByRegistrationNumber(String registrationNumber){
        return carRepository.findByRegistrationNumber(registrationNumber).orElseThrow(() -> new ResourceNotFoundException(Car.class.getSimpleName(), registrationNumber));
    }

    public void deleteCarById(String registrationNumber) {
        carRepository.deleteById(registrationNumber);
    }

    public Car addCar(Car car) {
        if (carRepository.findByRegistrationNumber(car.getRegistrationNumber()).isPresent()){
            throw new ConflictException(Car.class.getSimpleName(), car.getRegistrationNumber());
        }
        return carRepository.save(car);
    }

}
