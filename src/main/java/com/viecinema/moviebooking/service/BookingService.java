package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.model.Booking;
import com.viecinema.moviebooking.model.Movie;
import com.viecinema.moviebooking.model.User;
import com.viecinema.moviebooking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Integer bookingId) {
        return bookingRepository.findById(bookingId).orElse(null);
    }

    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Integer bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUser(user);
    }

    public List<Booking> getBookingsByMovie(Movie movie) {
        return bookingRepository.findByShowtimeMovie(movie);
    }

    public List<Booking> getBookingsAfter(LocalDateTime bookingDate) {
        return bookingRepository.findByBookingDateAfter(bookingDate);
    }

    public List<Booking> getBookingsBefore(LocalDateTime bookingDate) {
        return bookingRepository.findByBookingDateBefore(bookingDate);
    }

    public List<Booking> getBookingsBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return bookingRepository.findByBookingDateBetween(startTime, endTime);
    }

    public List<Booking> getBookingsByShowTimeAfter(LocalDateTime startTime) {
        return bookingRepository.findByShowtimeStartTimeAfter(startTime);
    }

    public List<Booking> getBookingsByShowTimeBefore(LocalDateTime startTime) {
        return bookingRepository.findByShowtimeStartTimeBefore(startTime);
    }

    public List<Booking> getBookingsByShowTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return bookingRepository.findByShowtimeStartTimeBetween(startTime, endTime);
    }

    // nếu cần thêm mn tự làm thêm nha
}
