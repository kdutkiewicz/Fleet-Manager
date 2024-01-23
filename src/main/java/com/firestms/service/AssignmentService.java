package com.firestms.service;

import com.firestms.model.Assignment;
import com.firestms.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssignmentService {

    @Autowired
    AssignmentRepository assignmentRepository;

    public Assignment addNewAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

}
