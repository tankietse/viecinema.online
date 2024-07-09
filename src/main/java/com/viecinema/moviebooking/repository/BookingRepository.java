package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.Booking;
import com.viecinema.moviebooking.model.Movie;
import com.viecinema.moviebooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByUser(User user);

    List<Booking> findByShowtimeMovie(Movie movie);


    List<Booking> findByBookingDateAfter(LocalDate bookingDate);

    List<Booking> findByBookingDateBefore(LocalDate bookingDate);

    List<Booking> findByBookingDateBetween(LocalDate startDate, LocalDate endDate);

    List<Booking> findByShowtimeStartDateAfter(LocalDate startDate);

    List<Booking> findByShowtimeStartDateBefore(LocalDate startDate);

    List<Booking> findByShowtimeStartDateBetween(LocalDate startDate, LocalDate endDate);
}
