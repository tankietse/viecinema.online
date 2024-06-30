package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.MovieDynamicData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieDynamicDataRepository extends JpaRepository<MovieDynamicData, Integer> {
    Optional<MovieDynamicData> findByMovieId(int movieId);
}
