package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.Trailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrailerRepository extends JpaRepository<Trailer, Integer> {
    List<Trailer> findByMovieId(Integer id);
}
