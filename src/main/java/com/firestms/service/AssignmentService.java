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

import java.time.Instant;

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

        Instant startTime = assignment.getStartTime();
        Instant endTime = assignment.getEndTime();

        //check if car isn't assignment in this time
        if (assignmentRepository.findAllAssignmentsForCarBetweenDates(assignment.getCarRegistrationNumber(), startTime, endTime).size() > 0) {
            throw new AssignmentException("The car with registration number:" + assignment.getCarRegistrationNumber() + " already has an assignment in this period of time");
        }
        //check if trailer isn't assignment to car in this time
        if (assignmentRepository.findAllAssignmentsForTrailerBetweenDates(assignment.getTrailerRegistrationNumber(), startTime, endTime).size() > 0) {
            throw new AssignmentException("The trailer with registration number:" + assignment.getTrailerRegistrationNumber() + " already has an assignment in this period of time");
        }

        return assignmentRepository.save(assignment);
    }

    public Assignment overrideAssignment(Assignment assignment, boolean crossHitch){
        return assignmentRepository.save(assignment);
    }
}
