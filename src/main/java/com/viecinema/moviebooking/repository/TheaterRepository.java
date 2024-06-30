package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Long> {
    Optional<Theater> findByName(String name);
    List<Theater> findByCity(String city);
    List<Theater> findByState(String state);
    List<Theater> findByCountry(String country);
    List<Theater> findByCapacityGreaterThanEqual(Integer capacity);
    List<Theater> findByCapacityLessThanEqual(Integer capacity);
    List<Theater> findByCityAndCapacityGreaterThanEqual(String city, Integer capacity);
    List<Theater> findByCityAndCapacityLessThanEqual(String city, Integer capacity);
    List<Theater> findByCityAndStateAndCountry(String city, String state, String country);
    List<Theater> findByCityAndStateAndCountryAndCapacityGreaterThanEqual(String city, String state, String country, Integer capacity);
    List<Theater> findByCityAndStateAndCountryAndCapacityLessThanEqual(String city, String state, String country, Integer capacity);
}
