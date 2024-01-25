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
        checkIfResourcesExists(assignment.getCarRegistrationNumber(), assignment.getTrailerRegistrationNumber());

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

    public Assignment overrideAssignment(Assignment assignment, boolean crossHitch) {
        checkIfResourcesExists(assignment.getCarRegistrationNumber(), assignment.getTrailerRegistrationNumber());
        List<Assignment> assignmentsForCar = assignmentRepository.findAllAssignmentsForCarBetweenDates(assignment.getCarRegistrationNumber(), assignment.getStartTime(), assignment.getEndTime());

        if (assignmentsForCar.size() > 0) {
            assignmentsForCar.forEach(carAssignment -> {
                modifyExistingAssignments(carAssignment, assignment);
                List<Assignment> assignmentsForTrailer = assignmentRepository.findAllAssignmentsForTrailerBetweenDates(assignment.getTrailerRegistrationNumber(), assignment.getStartTime(), assignment.getEndTime());
                if (assignmentsForTrailer.size() > 0) {
                    assignmentsForTrailer.forEach(trailAssignment -> {
                        modifyExistingAssignments(trailAssignment, assignment);
                        if (crossHitch) {
                            assignmentRepository.save(Assignment.builder().carRegistrationNumber(trailAssignment.getCarRegistrationNumber())
                                .trailerRegistrationNumber(carAssignment.getTrailerRegistrationNumber()).startTime(assignment.getStartTime()).endTime(assignment.getEndTime()).build());
                        }
                    });
                }
            });
        }
        return assignmentRepository.save(assignment);
    }

    private void modifyExistingAssignments(Assignment assignmentFromDB, Assignment newAssignment) {
        int compareStartDate = assignmentFromDB.getStartTime().compareTo(newAssignment.getStartTime());
        int compareEndDate = assignmentFromDB.getEndTime().compareTo(newAssignment.getEndTime());
        if (compareStartDate >= 0 && compareEndDate <= 0) {
            assignmentRepository.deleteById(assignmentFromDB.getId());
        } else if (compareStartDate >= 0 && compareEndDate >= 0) {
            assignmentFromDB.setStartTime(newAssignment.getEndTime().plusSeconds(1));
            assignmentRepository.save(assignmentFromDB);
        } else if (compareStartDate <= 0 && compareEndDate <= 0) {
            assignmentFromDB.setEndTime(newAssignment.getStartTime().minusSeconds(1));
            assignmentRepository.save(assignmentFromDB);
        } else if (compareStartDate <= 0 && compareEndDate >= 0) {
            assignmentRepository.save(Assignment.builder().carRegistrationNumber(assignmentFromDB.getCarRegistrationNumber())
                .trailerRegistrationNumber(assignmentFromDB.getTrailerRegistrationNumber()).startTime(newAssignment.getEndTime().plusSeconds(1)).endTime(assignmentFromDB.getEndTime()).build());
            assignmentFromDB.setEndTime(newAssignment.getStartTime().minusSeconds(1));
            assignmentRepository.save(assignmentFromDB);
        }
    }


    private void checkIfResourcesExists(String carRegistrationNumber, String trailerRegistrationNumber) {
        carRepository.findByRegistrationNumber(carRegistrationNumber)
            .orElseThrow(() -> new ResourceNotFoundException(Car.class.getSimpleName(), carRegistrationNumber));

        trailerRepository.findByRegistrationNumber(trailerRegistrationNumber)
            .orElseThrow(() -> new ResourceNotFoundException(Trailer.class.getSimpleName(), trailerRegistrationNumber));
    }
}
