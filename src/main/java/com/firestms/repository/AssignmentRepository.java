package com.firestms.repository;

import com.firestms.model.Assignment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AssignmentRepository extends CrudRepository<Assignment, UUID> {

    List<Assignment> findAllByCarRegistrationNumber(String carRegistrationNumber);

    List<Assignment> findAllByTrailerRegistrationNumber(String trailerRegistrationNumber);

    @Query("select a from Assignment a where " +
        "(a.carRegistrationNumber = :carRegistrationNumber) " +
        "and " +
        "((a.startTime <= :startDate and a.endTime >= :startDate) " +
        "or (a.startTime <= :endDate and a.endTime >= :endDate) " +
        "or (a.startTime <= :startDate and a.endTime >= :endDate)" +
        "or (a.startTime >= :startDate and a.endTime <= :endDate)) ")
    List<Assignment> findAllAssignmentsForCarBetweenDates(@Param("carRegistrationNumber") String carRegistrationNumber, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    @Query("select a from Assignment a where " +
        "(a.trailerRegistrationNumber = :trailerRegistrationNumber) " +
        "and " +
        "((a.startTime <= :startDate and a.endTime >= :startDate) " +
        "or (a.startTime <= :endDate and a.endTime >= :endDate) " +
        "or (a.startTime <= :startDate and a.endTime >= :endDate)" +
        "or (a.startTime >= :startDate and a.endTime <= :endDate)) ")
    List<Assignment> findAllAssignmentsForTrailerBetweenDates(@Param("trailerRegistrationNumber") String trailerRegistrationNumber, @Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    List<Assignment> findALlByCarRegistrationNumberAndEndTimeIsAfter(String carRegistrationNumber, Instant endDate);


}
