package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.ScreenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenTypeRepository extends JpaRepository<ScreenType, Integer> {
}

