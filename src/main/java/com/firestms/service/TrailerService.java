package com.firestms.service;

import com.firestms.Exception.ConflictException;
import com.firestms.Exception.ResourceNotFoundException;
import com.firestms.model.Trailer;
import com.firestms.repository.TrailerRepository;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrailerService {

    TrailerRepository trailerRepository;

    public TrailerService(TrailerRepository trailerRepository) {
        this.trailerRepository = trailerRepository;
    }


    public List<Trailer> getAllTrailers() {
        return Lists.newArrayList(trailerRepository.findAll());
    }

    public Trailer addNewTrailer(Trailer trailer) {
        if (trailerRepository.findByRegistrationNumber(trailer.getRegistrationNumber()).isPresent()) {
            throw new ConflictException(Trailer.class.getSimpleName(), trailer.getRegistrationNumber());
        }
        return trailerRepository.save(trailer);
    }

    public void deleteTrailerByRegistrationNumber(String registrationNumber) {
        trailerRepository.deleteById(registrationNumber);
    }

    public Trailer findTrailerByRegistrationNumber(String registrationNumber) {
        return trailerRepository.findByRegistrationNumber(registrationNumber).orElseThrow(() -> new ResourceNotFoundException(Trailer.class.getSimpleName(), registrationNumber));
    }
}
