package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.model.Booking;
import com.viecinema.moviebooking.model.Movie;
import com.viecinema.moviebooking.model.User;
import com.viecinema.moviebooking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public List<Booking> getBookingsAfter(LocalDate bookingDate) {
        return bookingRepository.findByBookingDateAfter(bookingDate);
    }

    public List<Booking> getBookingsBefore(LocalDate bookingDate) {
        return bookingRepository.findByBookingDateBefore(bookingDate);
    }

    public List<Booking> getBookingsBetween(LocalDate startDate, LocalDate endDate) {
        return bookingRepository.findByBookingDateBetween(startDate, endDate);
    }

    public List<Booking> getBookingsByShowDateAfter(LocalDate startDate) {
        return bookingRepository.findByShowtimeStartDateAfter(startDate);
    }

    public List<Booking> getBookingsByShowDateBefore(LocalDate startDate) {
        return bookingRepository.findByShowtimeStartDateBefore(startDate);
    }

    public List<Booking> getBookingsByShowDateBetween(LocalDate startDate, LocalDate endDate) {
        return bookingRepository.findByShowtimeStartDateBetween(startDate, endDate);
    }

    // nếu cần thêm mn tự làm thêm nha
}
