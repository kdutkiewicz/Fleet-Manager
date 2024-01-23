package com.firestms.controller;

import com.firestms.model.Assignment;
import com.firestms.model.CarWithAssignments;
import com.firestms.model.Car;
import com.firestms.repository.CarRepository;
import com.firestms.service.AssignmentService;
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

    public FleetManagerController(AssignmentService assignmentService, CarService carService) {
        this.assignmentService = assignmentService;
        this.carService = carService;
    }

    private CarRepository carRepository;
    private AssignmentService assignmentService;

    private CarService carService;


    @PostMapping("/car")
    public Car addCar(@RequestBody Car car) {
        return carService.addCar(car);
    }

    @GetMapping("/cars")
    public List<CarWithAssignments> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/car/{id}")
    public Optional<CarWithAssignments> getCarById(@PathVariable("id") String id) {
        return carService.findByRegistrationNumber(id);
    }

    @PostMapping("/assignment")
    public Assignment addNewAssignment(@RequestBody Assignment assignment) {
        return assignmentService.addNewAssignment(assignment);
    }
}
