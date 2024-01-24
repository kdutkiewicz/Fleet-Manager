package com.firestms.service;

import com.firestms.Exception.AssignmentException;
import com.firestms.Exception.ResourceNotFoundException;
import com.firestms.model.Assignment;
import com.firestms.model.Car;
import com.firestms.model.Trailer;
import com.firestms.repository.AssignmentRepository;
import com.firestms.repository.CarRepository;
import com.firestms.repository.TrailerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentService {

    AssignmentRepository assignmentRepository;
    CarRepository carRepository;
    TrailerRepository trailerRepository;

    public AssignmentService(AssignmentRepository assignmentRepository, CarRepository carRepository, TrailerRepository trailerRepository) {
        this.assignmentRepository = assignmentRepository;
        this.carRepository = carRepository;
        this.trailerRepository = trailerRepository;
    }

    public Assignment addNewAssignment(Assignment assignment) {
        //check if resources exists
        carRepository.findByRegistrationNumber(assignment.getCarRegistrationNumber())
            .orElseThrow(() -> new ResourceNotFoundException(Car.class.getSimpleName(), assignment.getCarRegistrationNumber()));

        trailerRepository.findByRegistrationNumber(assignment.getTrailerRegistrationNumber())
            .orElseThrow(() -> new ResourceNotFoundException(Trailer.class.getSimpleName(), assignment.getTrailerRegistrationNumber()));

        //check if trailer isnt assignet in this time
        if(assignmentRepository.findAllByCarRegistrationNumberAndStartTimeBetweenOrEndTimeBetween(assignment.getCarRegistrationNumber(), assignment.getStartTime(),assignment.getEndTime(), assignment.getStartTime(),assignment.getEndTime()).size() > 0) {
            throw new AssignmentException("The Car with registration number:"+assignment.getCarRegistrationNumber()+" already has an assignment in this period of time");
        }

        return assignmentRepository.save(assignment);
    }
}
