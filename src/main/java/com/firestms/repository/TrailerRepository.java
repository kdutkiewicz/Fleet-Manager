package com.firestms.repository;

import com.firestms.model.Trailer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrailerRepository extends CrudRepository<Trailer, String> {

    public Optional<Trailer> findByRegistrationNumber(String registrationNumber);
}
