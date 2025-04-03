package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.MovieDynamicData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieDynamicDataRepository extends JpaRepository<MovieDynamicData, Long> {
    Optional<MovieDynamicData> findByMovieId(Long movieId);
}
