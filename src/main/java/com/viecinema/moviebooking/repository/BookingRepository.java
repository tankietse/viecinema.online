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
    List<Booking> findByMovie(Movie movie);
    List<Booking> findByBookingTimeAfter(LocalDateTime bookingTime);
    List<Booking> findByBookingTimeBefore(LocalDateTime bookingTime);
    List<Booking> findByBookingTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    List<Booking> findByShowTimeAfter(LocalDateTime showTime);
    List<Booking> findByShowTimeBefore(LocalDateTime showTime);
    List<Booking> findByShowTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
