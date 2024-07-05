package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.model.Theater;
import com.viecinema.moviebooking.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TheaterService {

    @Autowired
    private TheaterRepository theaterRepository;

    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }

    public Optional<Theater> getTheaterById(Integer id) {
        return theaterRepository.findById(id);
    }
    public Optional<Theater> getTheaterByName(String name) {
        return theaterRepository.findByName(name);
    }

    public Theater saveTheater(Theater theater) {
        return theaterRepository.save(theater);
    }

    public void deleteTheater(Integer id) {
        theaterRepository.deleteById(id);
    }

    public List<Theater> getTheatersByCity(String city) {
        return theaterRepository.findByCity(city);
    }

    public List<Theater> getTheatersByCapacityGreaterThanEqual(Integer capacity) {
        return theaterRepository.findByCapacityGreaterThanEqual(capacity);
    }

    public List<Theater> getTheatersByCapacityLessThanEqual(Integer capacity) {
        return theaterRepository.findByCapacityLessThanEqual(capacity);
    }

    public List<Theater> getTheatersByCityAndCapacity(String city, Integer capacity) {
        return theaterRepository.findByCityAndCapacityGreaterThanEqual(city, capacity);
    }

    public List<Theater> getTheatersByCityAndCapacityLessThanEqual(String city, Integer capacity) {
        return theaterRepository.findByCityAndCapacityLessThanEqual(city, capacity);
    }
}
