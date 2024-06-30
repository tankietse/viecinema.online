package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.Screen;
import com.viecinema.moviebooking.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Integer> {
    List<Screen> findByTheater(Theater theater);
}
