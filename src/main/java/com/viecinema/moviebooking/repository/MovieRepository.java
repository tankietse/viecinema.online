package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    Optional<Movie> findByTmdbID(int tmdbID);
    List<Movie> findByReleaseDateBetween(LocalDate startDate, LocalDate endDate);

    List<Movie> findByReleaseDateAfter(LocalDate date);
}
