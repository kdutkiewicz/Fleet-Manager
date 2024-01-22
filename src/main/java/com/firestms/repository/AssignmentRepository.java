package com.firestms.repository;

import com.firestms.model.AssignmentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AssignmentRepository extends CrudRepository<AssignmentEntity, UUID> {
}
