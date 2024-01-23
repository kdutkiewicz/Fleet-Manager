package com.firetms;

import com.firestms.FleetManager;
import com.firestms.controller.FleetManagerController;
import com.firestms.model.Assignment;
import com.firestms.model.Car;
import com.firestms.repository.AssignmentRepository;
import com.firestms.repository.CarRepository;
import com.firestms.service.AssignmentService;
import com.firestms.service.CarService;
import com.google.common.base.CharMatcher;
import org.aspectj.lang.annotation.Before;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration()
@AutoConfigureMockMvc
@Profile("test-in-memory-h2")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = TestWebConfig.class)
public class IntegartionTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;


    @Autowired
    private CarRepository carRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void addCarAndGetItViaApi() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/fleet-manager/car")
                .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"registrationNumber\":\"SO123\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/fleet-manager/car/SO123")
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.registrationNumber", CoreMatchers.is("SO123")));
    }

    @Test
    public void asd() {

    }

    void addCar(String registrationNumber) {
        carRepository.save(new Car(registrationNumber));
    }

    void addAssignment(String carRegistrationNumber, String trailerRegistrationNumber, Instant startTime, Instant endTime) {
        assignmentRepository.save(
            Assignment.builder()
                .carRegistrationNumber(carRegistrationNumber)
                .trailerRegistrationNumber(trailerRegistrationNumber)
                .startTime(startTime)
                .endTime(endTime).build()
        );
    }
}
