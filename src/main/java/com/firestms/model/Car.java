package com.firestms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Car {

    @Id
    private String id;
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//
//    public Long getId() {
//        return id;
//    }
}
