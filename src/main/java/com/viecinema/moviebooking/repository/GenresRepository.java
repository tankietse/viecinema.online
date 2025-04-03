package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.Genre;
import com.viecinema.moviebooking.model.Trailer;
import com.viecinema.moviebooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenresRepository extends JpaRepository<Genre,Integer> {
    Optional<Trailer> findByGenreId(int genreId);
    Optional<User> findByName(String name);
}
