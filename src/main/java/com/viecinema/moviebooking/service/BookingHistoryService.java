package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.dto.BookingHistoryDto;
import com.viecinema.moviebooking.model.Booking;
import com.viecinema.moviebooking.model.BookingHistory;
import com.viecinema.moviebooking.repository.BookingHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingHistoryService {

    private final BookingHistoryRepository bookingHistoryRepository;

    @Autowired
    public BookingHistoryService(BookingHistoryRepository bookingHistoryRepository) {
        this.bookingHistoryRepository = bookingHistoryRepository;
    }

    public List<BookingHistoryDto> getBookingHistoryByBooking(Booking booking) {
        return bookingHistoryRepository.findByBooking(booking).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Can them CRUD tự làm nha ae :]]

    private BookingHistoryDto convertToDto(BookingHistory bookingHistory) {
        return new BookingHistoryDto(
                bookingHistory.getHistoryId(),
                bookingHistory.getBooking().getBookingId(),
                bookingHistory.getUser().getId(),
                bookingHistory.getStatus(),
                bookingHistory.getChangeDate()
        );
    }
}

