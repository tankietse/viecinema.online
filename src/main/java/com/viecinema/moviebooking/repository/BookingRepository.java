package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.Booking;
import com.viecinema.moviebooking.model.Movie;
import com.viecinema.moviebooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByUser(User user);

    List<Booking> findByShowtimeMovie(Movie movie);


    List<Booking> findByBookingDateAfter(LocalDateTime bookingDate);

    List<Booking> findByBookingDateBefore(LocalDateTime bookingDate);

    List<Booking> findByBookingDateBetween(LocalDateTime startTime, LocalDateTime endTime);

    List<Booking> findByShowtimeStartTimeAfter(LocalDateTime startTime);

    List<Booking> findByShowtimeStartTimeBefore(LocalDateTime startTime);

    List<Booking> findByShowtimeStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
