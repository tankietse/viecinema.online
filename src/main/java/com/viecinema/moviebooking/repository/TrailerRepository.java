package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.Trailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrailerRepository extends JpaRepository<Trailer, Long> {
    List<Trailer> findByMovieId(Long id);
}
