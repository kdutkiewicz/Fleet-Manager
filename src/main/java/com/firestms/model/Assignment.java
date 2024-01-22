package com.firestms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assignment {

    private String carId;
    private String trailerId;
    private Optional<Date> startTime;
    private Optional<Date> endTime;
}
