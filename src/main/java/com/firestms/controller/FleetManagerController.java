package com.firestms.controller;

import com.firestms.model.CarPOJO;
import com.firestms.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/fleet-manager")
public class FleetManagerController {

    @Autowired
    private CarService carService;

    @PostMapping("/car")
    public CarPOJO addCar(@RequestBody CarPOJO car) {
        return carService.addCar(car);
    }

    @GetMapping("/cars")
    public List<CarPOJO> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/car/{id}")
    public Optional<CarPOJO> getCarById(@PathVariable String id) {
        return carService.findByRegistrationNumber(id);
    }
}
