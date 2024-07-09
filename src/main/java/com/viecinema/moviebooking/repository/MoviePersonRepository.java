package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.MoviePerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoviePersonRepository extends JpaRepository<MoviePerson, Integer> {
}
