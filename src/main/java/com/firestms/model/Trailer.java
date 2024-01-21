package com.firestms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Trailer {

    @Id
    private String id;
}
