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


    @Test
    public void successfullyAddAssignments() {
        addCar("CAR1234");
        addTrailer("TR1234");
        assignmentService.addNewAssignment(getAssignment("CAR1234", "TR1234", Instant.parse("2024-01-05T00:00:00Z"), Instant.parse("2024-01-08T00:00:00Z")));
        assignmentService.addNewAssignment(getAssignment("CAR1234", "TR1234", Instant.parse("2024-01-09T00:00:00Z"), Instant.parse("2024-01-12T00:00:00Z")));
        Assertions.assertEquals(assignmentRepository.findAllByCarRegistrationNumber("CAR1234").size(), 2);


    }

    @Test
    public void addingAssignmentWhenThereIsAlreadyAssignmentForCarShouldThrowAnException() {
        addCar("CAR123");
        addTrailer("TR123");
        var carRegistrationNumber = "CAR123";
        var trailerRegistrationNumber = "TR123";
        var startTime1 = "2024-01-01T00:00:00Z";
        var endTime1 = "2024-01-02T00:00:00Z";
        var startTime2 = "2024-01-05T00:00:00Z";
        var endTime2 = "2024-01-09T00:00:00Z";
        assignmentRepository.save(
            Assignment.builder()
                .carRegistrationNumber(carRegistrationNumber)
                .trailerRegistrationNumber(trailerRegistrationNumber)
                .startTime(Instant.parse(startTime1))
                .endTime(Instant.parse(endTime1)).build()
        );
        assignmentRepository.save(
            Assignment.builder()
                .carRegistrationNumber(carRegistrationNumber)
                .trailerRegistrationNumber(trailerRegistrationNumber)
                .startTime(Instant.parse(startTime2))
                .endTime(Instant.parse(endTime2)).build()
        );
        Exception exception = Assertions.assertThrows(AssignmentException.class, () ->
            assignmentService.addNewAssignment(getAssignment("CAR123", "TR123", Instant.parse("2024-01-03T00:00:00Z"), Instant.parse("2024-01-06T00:00:00Z"))));
        String expectedMessage = "The car with registration number:CAR123 already has an assignment in this period of time";

        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void addingAssignmentWhenThereIsAlreadyAssignmentForTrailerShouldThrowAnException() {
        addCar("CAR11");
        addCar("CAR12");
        addTrailer("TR11");
        addAssignmentDirectlyToRepo("CAR11", "TR11", Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-01-06T00:00:00Z"));
        List<Assignment> asd = Lists.newArrayList(assignmentRepository.findAll());
        Exception exception = Assertions.assertThrows(AssignmentException.class, () ->
            assignmentService.addNewAssignment(getAssignment("CAR12", "TR11", Instant.parse("2024-01-03T00:00:00Z"), Instant.parse("2024-01-06T00:00:00Z"))));
        String expectedMessage = "The trailer with registration number:TR11 already has an assignment in this period of time";

        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    void addCar(String registrationNumber) {
        carRepository.save(new Car(registrationNumber));
    }

    void addTrailer(String registrationNumber) {
        trailerRepository.save(new Trailer(registrationNumber));
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
