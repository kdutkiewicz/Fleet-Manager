package com.firestms.controller;

import com.firestms.model.Car;
import com.firestms.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/fleet-manager")
public class FleetManagerController {
    @Autowired
    CarRepository carRepository;

    @PostMapping("/car")
    public Car addCar(@RequestBody Car car) {
        return carRepository.save(car);
    }

    @GetMapping("/cars")
    public void getAllCars() {
        carRepository.findAll().forEach(c -> System.out.println(c.getId()));
    }
}
