package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.Trailer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrailerRepository extends JpaRepository<Trailer, Integer> {
    Optional<Trailer> findByTrailerId(int trailerId);
    List<Trailer> findByMovieId(int movieId);
}
