package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.Movie;
import com.viecinema.moviebooking.model.Schedule;
import com.viecinema.moviebooking.model.Screen;
import com.viecinema.moviebooking.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findByMovie(Movie movie);
    List<Schedule> findByScreen(Screen screen);
    List<Schedule> findByTheater(Theater theater);
    List<Schedule> findByStartTimeAfter(LocalDateTime startTime);
    List<Schedule> findByEndTimeBefore(LocalDateTime endTime);
    List<Schedule> findByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    List<Schedule> findByMovieAndStartTimeBetween(Movie movie, LocalDateTime startTime, LocalDateTime endTime);
    List<Schedule> findByScreenAndStartTimeBetween(Screen screen, LocalDateTime startTime, LocalDateTime endTime);
    List<Schedule> findByTheaterAndStartTimeBetween(Theater theater, LocalDateTime startTime, LocalDateTime endTime);
}
