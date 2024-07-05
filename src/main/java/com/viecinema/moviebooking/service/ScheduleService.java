package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.dto.ScheduleDTO;
import com.viecinema.moviebooking.model.Movie;
import com.viecinema.moviebooking.model.Schedule;
import com.viecinema.moviebooking.model.Screen;
import com.viecinema.moviebooking.model.Theater;
import com.viecinema.moviebooking.repository.MovieRepository;
import com.viecinema.moviebooking.repository.ScheduleRepository;
import com.viecinema.moviebooking.repository.ScreenRepository;
import com.viecinema.moviebooking.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Optional<Schedule> getScheduleById(Integer id) {
        return scheduleRepository.findById(id);
    }

    public Schedule saveSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        Optional<Movie> movie = movieRepository.findById(scheduleDTO.getMovieId());
        Optional<Screen> screen = screenRepository.findById(scheduleDTO.getScreenId());
        Optional<Theater> theater = theaterRepository.findById(scheduleDTO.getTheaterId());

        if (movie.isPresent() && screen.isPresent() && theater.isPresent()) {
            schedule.setMovie(movie.get());
            schedule.setScreen(screen.get());
            schedule.setTheater(theater.get());
            schedule.setStartTime(scheduleDTO.getStartTime());
            schedule.setEndTime(scheduleDTO.getEndTime());
            return scheduleRepository.save(schedule);
        } else {
            throw new RuntimeException("Invalid movie, screen or theater ID");
        }
    }
    public ScheduleDTO createSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        Optional<Movie> movie = movieRepository.findById(scheduleDTO.getMovieId());
        Optional<Screen> screen = screenRepository.findById(scheduleDTO.getScreenId());
        Optional<Theater> theater = theaterRepository.findById(scheduleDTO.getTheaterId());

        // Kiểm tra xem movie, screen và theater có tồn tại hay không
        if (movie.isPresent() && screen.isPresent() && theater.isPresent()) {
            schedule.setMovie(movie.get());
            schedule.setScreen(screen.get());
            schedule.setTheater(theater.get());
            schedule.setStartTime(scheduleDTO.getStartTime());
            schedule.setEndTime(scheduleDTO.getEndTime());

            Schedule savedSchedule = scheduleRepository.save(schedule);

            return new ScheduleDTO(
                    savedSchedule.getScheduleId(),
                    savedSchedule.getMovie().getId(),
                    savedSchedule.getScreen().getId(),
                    Math.toIntExact(savedSchedule.getTheater().getTheaterId()),
                    savedSchedule.getStartTime(),
                    savedSchedule.getEndTime()
            );
        } else {
            throw new RuntimeException("Invalid movie, screen or theater ID");
        }
    }

    public void deleteSchedule(Integer id) {
        scheduleRepository.deleteById(id);
    }

    public List<Schedule> getSchedulesByMovie(Movie movie) {
        return scheduleRepository.findByMovie(movie);
    }

    public List<Schedule> getSchedulesByScreen(Screen screen) {
        return scheduleRepository.findByScreen(screen);
    }

    public List<Schedule> getSchedulesByTheater(Theater theater) {
        return scheduleRepository.findByTheater(theater);
    }

    public List<Schedule> getSchedulesByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return scheduleRepository.findByStartTimeBetween(startTime, endTime);
    }
}
