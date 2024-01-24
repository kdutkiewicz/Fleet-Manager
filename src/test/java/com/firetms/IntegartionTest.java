package com.firetms;

import com.firestms.Exception.AssignmentException;
import com.firestms.FleetManager;
import com.firestms.controller.FleetManagerController;
import com.firestms.model.Assignment;
import com.firestms.model.Car;
import com.firestms.model.Trailer;
import com.firestms.repository.AssignmentRepository;
import com.firestms.repository.CarRepository;
import com.firestms.repository.TrailerRepository;
import com.firestms.service.AssignmentService;
import com.firestms.service.CarService;
import com.google.common.base.CharMatcher;
import org.aspectj.lang.annotation.Before;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.util.UUID;

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
    private TrailerRepository trailerRepository;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void addCarAndGetItViaApi() throws Exception {

        var registrationNumber = "SO123";

        mockMvc.perform(MockMvcRequestBuilders.post("/fleet-manager/car")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"registrationNumber\":\"" + registrationNumber + "\"}"))
            .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/fleet-manager/car/" + registrationNumber)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.registrationNumber", CoreMatchers.is(registrationNumber)));
    }

    @Test
    public void addingCarWithExistingRegistrationNumberShouldReturnConflictStatus() throws Exception {
        var registrationNumber = "SO111";
        addCar(registrationNumber);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/fleet-manager/car")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"registrationNumber\":\"" + registrationNumber + "\"}"))
            .andExpect(MockMvcResultMatchers.status().isConflict())
            .andReturn();

        Assertions.assertEquals(result.getResponse().getContentAsString(), "Resource of type:Car and id:SO111 already exist in database");
    }

    @Test
    public void addingTrailerWithExistingRegistrationNumberShouldReturnConflictStatus() throws Exception {
        var registrationNumber = "TR111";
        addTrailer(registrationNumber);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/fleet-manager/trailer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"registrationNumber\":\"" + registrationNumber + "\"}"))
            .andExpect(MockMvcResultMatchers.status().isConflict())
            .andReturn();

        Assertions.assertEquals(result.getResponse().getContentAsString(), "Resource of type:Trailer and id:TR111 already exist in database");
    }

    @Test
    public void aferDeletingCarShouldReturnNotFound() throws Exception {
        var registrationNumber = "CAR1";
        addCar(registrationNumber);
        mockMvc.perform(MockMvcRequestBuilders.delete("/fleet-manager/car/" + registrationNumber))
            .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/fleet-manager/car/" + registrationNumber))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void aferDeletingTrailerShouldReturnNotFound() throws Exception {
        var registrationNumber = "TRAILER1";
        addCar(registrationNumber);
        mockMvc.perform(MockMvcRequestBuilders.delete("/fleet-manager/trailer/" + registrationNumber))
            .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.get("/fleet-manager/trailer/" + registrationNumber))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    void addCar(String registrationNumber) {
        carRepository.save(new Car(registrationNumber));
    }

    void addTrailer(String registrationNumber) {
        trailerRepository.save(new Trailer(registrationNumber));
    }
}
