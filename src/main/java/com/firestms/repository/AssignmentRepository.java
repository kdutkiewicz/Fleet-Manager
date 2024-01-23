package com.firestms.repository;

import com.firestms.model.Assignment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface AssignmentRepository extends CrudRepository<Assignment, UUID> {

    List<Assignment> findAllByCarRegistrationNumber(String carRegistrationNumber);
}
