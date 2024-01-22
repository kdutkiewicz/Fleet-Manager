package com.firestms.controller;

import com.firestms.model.Assignment;
import com.firestms.model.AssignmentEntity;
import com.firestms.model.Car;
import com.firestms.model.CarEntity;
import com.firestms.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/fleet-manager")
public class FleetManagerController {

    @Autowired
    private CarService carService;

    @Autowired
    private 

    @PostMapping("/car")
    public CarEntity addCar(@RequestBody CarEntity car) {
        return carService.addCar(car);
    }

    @GetMapping("/cars")
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/car/{id}")
    public Optional<Car> getCarById(@PathVariable String id) {
        return carService.findByRegistrationNumber(id);
    }

    @PostMapping("/assignment")
    public Assignment addNewAssignment(@RequestBody Assignment assignment) {
        return assignment;

    }
}
