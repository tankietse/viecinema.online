package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Integer> {
    Optional<Theater> findByTheaterName(String theaterName);
    boolean existsByTheaterName(String theaterName);
    List<Theater> findByCity(String city);
    List<Theater> findByCapacityGreaterThanEqual(Integer capacity);
    List<Theater> findByCapacityLessThanEqual(Integer capacity);
    List<Theater> findByCityAndCapacityGreaterThanEqual(String city, Integer capacity);
    List<Theater> findByCityAndCapacityLessThanEqual(String city, Integer capacity);
}
