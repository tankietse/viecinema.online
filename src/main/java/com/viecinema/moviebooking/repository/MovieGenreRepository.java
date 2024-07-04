package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.MovieGenre;
import com.viecinema.moviebooking.model.MovieGenreId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieGenreRepository extends JpaRepository<MovieGenre, MovieGenreId> {
}
