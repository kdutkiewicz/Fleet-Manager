package com.firetms;

import com.firestms.Exception.AssignmentException;
import com.firestms.model.Assignment;
import com.firestms.model.Car;
import com.firestms.model.Trailer;
import com.firestms.repository.AssignmentRepository;
import com.firestms.repository.CarRepository;
import com.firestms.repository.TrailerRepository;
import com.firestms.service.AssignmentService;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.Instant;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration()
@Profile("test-in-memory-h2")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = TestWebConfig.class)
public class AssignmentServiceTest {
    
    private final String CAR1= "CAR1";
    private final String CAR2 = "CAR2";
    private final String TRAILER1 = "TRAILER1";
    private final String TRAILER2 = "TRAILER2";

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private TrailerRepository trailerRepository;

    @Autowired
    private AssignmentService assignmentService;

    @BeforeEach
    public void setUp() {
        assignmentRepository.deleteAll();
    }

    @BeforeAll
    public void setUpAll(){
        carRepository.save(new Car(CAR1));
        carRepository.save(new Car(CAR2));
        trailerRepository.save(new Trailer(TRAILER1));
        trailerRepository.save(new Trailer(TRAILER2));
    }

    @Test
    public void successfullyAddAssignments() {
        assignmentService.addNewAssignment(getAssignment(CAR1, TRAILER1, Instant.parse("2024-01-05T00:00:00Z"), Instant.parse("2024-01-08T00:00:00Z")));
        assignmentService.addNewAssignment(getAssignment(CAR1, TRAILER1, Instant.parse("2024-01-09T00:00:00Z"), Instant.parse("2024-01-12T00:00:00Z")));
        Assertions.assertEquals(assignmentRepository.findAllByCarRegistrationNumber(CAR1).size(), 2);
    }

    @Test
    public void addingAssignmentWhenThereIsAlreadyAssignmentForCarShouldThrowAnException() {
        var startTime1 = Instant.parse("2024-01-01T00:00:00Z");
        var endTime1 = Instant.parse("2024-01-02T00:00:00Z");
        var startTime2 = Instant.parse("2024-01-05T00:00:00Z");
        var endTime2 = Instant.parse("2024-01-09T00:00:00Z");
        
        addAssignmentDirectlyToRepo(CAR1, TRAILER1, startTime1, endTime1);
        addAssignmentDirectlyToRepo(CAR1, TRAILER1, startTime2, endTime2);
        
        Exception exception = Assertions.assertThrows(AssignmentException.class, () ->
            assignmentService.addNewAssignment(getAssignment(CAR1, TRAILER1, Instant.parse("2024-01-03T00:00:00Z"), Instant.parse("2024-01-06T00:00:00Z"))));
        String expectedMessage = "The car with registration number:"+CAR1+" already has an assignment in this period of time";

        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void addingAssignmentWhenThereIsAlreadyAssignmentForTrailerShouldThrowAnException() {
        addAssignmentDirectlyToRepo(CAR1, TRAILER1, Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-06T00:00:00Z"));
        List<Assignment> asd = Lists.newArrayList(assignmentRepository.findAll());
        Exception exception = Assertions.assertThrows(AssignmentException.class, () ->
            assignmentService.addNewAssignment(getAssignment(CAR2, TRAILER1, Instant.parse("2024-01-03T00:00:00Z"), Instant.parse("2024-01-06T00:00:00Z"))));
        String expectedMessage = "The trailer with registration number:"+TRAILER1+" already has an assignment in this period of time";

        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void overrideAssignmentWhenNewAssignmentTakeAllTimeOfOldOne() {
    }


    void addAssignmentDirectlyToRepo(String carRegistrationNumber, String trailerRegistrationNumber, Instant startTime, Instant endTime) {
        assignmentRepository.save(
            Assignment.builder()
                .carRegistrationNumber(carRegistrationNumber)
                .trailerRegistrationNumber(trailerRegistrationNumber)
                .startTime(startTime)
                .endTime(endTime).build()
        );
    }

    Assignment getAssignment(String carRegistrationNumber, String trailerRegistrationNumber, Instant startTime, Instant endTime) {
        return Assignment.builder()
            .carRegistrationNumber(carRegistrationNumber)
            .trailerRegistrationNumber(trailerRegistrationNumber)
            .startTime(startTime)
            .endTime(endTime).build();
    }
}
