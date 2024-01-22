package com.firestms.service;

import com.firestms.model.Assignment;
import com.firestms.model.AssignmentEntity;
import com.firestms.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AssignmentService {

    @Autowired
    AssignmentRepository assignmentRepository;

    public AssignmentEntity addNewAssignment(Assignment assignment) {
        return assignmentRepository.save(AssignmentMapper.mapAssignmentToAssignmentEntity(assignment));
    }

    private class AssignmentMapper {


        static AssignmentEntity mapAssignmentToAssignmentEntity(Assignment assignment) {
            return AssignmentEntity.builder()
                .carId(assignment.getCarId())
                .trailerId(assignment.getTrailerId())
                .startTime(assignment.getStartTime().map(Date::toInstant).orElse(null))
                .endTime(assignment.getEndTime().map(Date::toInstant).orElse(null))
                .build();
        }
    }

}
