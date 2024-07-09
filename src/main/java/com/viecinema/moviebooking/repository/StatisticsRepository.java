package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Integer> {
    List<Statistics> findByDateBetween(Date startDate, Date endDate);
}

