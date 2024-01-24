package com.firestms.repository;

import com.firestms.model.Assignment;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.repository.CrudRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AssignmentRepository extends CrudRepository<Assignment, UUID> {

    List<Assignment> findAllByCarRegistrationNumber(String carRegistrationNumber);

    List<Assignment> findAllByStartTimeAfterAndStartTimeBefore(Instant startTime, Instant endTime);

    List<Assignment> findAllByCarRegistrationNumberAndStartTimeBetweenOrEndTimeBetween(String carRegistrationNumber, Instant startTime1, Instant endTime1,Instant startTime2, Instant endTime2);

    List<Assignment> findAllByCarRegistrationNumberAndStartTimeIsAfterAndEndTimeBefore(String carRegistrationNumber, Instant startTime, Instant endTime);
    List<Assignment> findAllByCarRegistrationNumberAndStartTimeIsBeforeAndEndTimeAfter(String carRegistrationNumber, Instant startTime, Instant endTime);
}
