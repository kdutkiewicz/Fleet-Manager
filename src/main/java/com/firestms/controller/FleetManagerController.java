package com.firestms.controller;

import com.firestms.model.Assignment;
import com.firestms.model.CarWithAssignments;
import com.firestms.model.Car;
import com.firestms.model.Trailer;
import com.firestms.service.AssignmentService;
import com.firestms.service.CarService;
import com.firestms.service.TrailerService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/fleet-manager")
public class FleetManagerController {

    public FleetManagerController(AssignmentService assignmentService, CarService carService, TrailerService trailerService) {
        this.assignmentService = assignmentService;
        this.carService = carService;
        this.trailerService = trailerService;
    }

    private AssignmentService assignmentService;

    private CarService carService;

    private TrailerService trailerService;

    @PostMapping("/car")
    public Car addCar(@RequestBody Car car) {
        return carService.addCar(car);
    }

    @DeleteMapping("/car/{registrationNumber}")
    public void deleteCar(@PathVariable("registrationNumber") String registrationNumber) {
        carService.deleteCarById(registrationNumber);
    }

    @GetMapping("/cars")
    public List<CarWithAssignments> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/car/{id}")
    public CarWithAssignments getCarById(@PathVariable("id") String id) {
        return carService.findByRegistrationNumber(id);
    }

    @PostMapping("/assignment")
    public Assignment addNewAssignment(@RequestBody Assignment assignment) {
        return assignmentService.addNewAssignment(assignment);
    }

    @GetMapping("/trailer/{id}")
    public Trailer getTrailerById(@PathVariable("id") String id) {
        return trailerService.findTrailerByRegistrationNumber(id);
    }

    @GetMapping("/trailers")
    public List<Trailer> getAllTrailers() {
        return trailerService.getAllTrailers();
    }

    @PostMapping("/trailer")
    public Trailer addTrailer(@RequestBody Trailer trailer) {
        return trailerService.addNewTrailer(trailer);
    }

    @DeleteMapping("/trailer/{registrationNumber}")
    public void deleteTrailer(@PathVariable("registrationNumber") String registrationNumber) {
        trailerService.deleteTrailerByRegistrationNumber(registrationNumber);
    }

    @PostMapping("/assignment/override/{crosshitch}")
    public Assignment overrideAssignment(@RequestBody Assignment assignment, @PathVariable("crosshitch") boolean crosshitch) {
        return assignmentService.overrideAssignment(assignment, crosshitch);
    }
}
