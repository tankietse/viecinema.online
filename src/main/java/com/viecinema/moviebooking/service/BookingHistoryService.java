package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.dto.BookingHistoryDTO;
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

    public List<BookingHistoryDTO> getBookingHistoryByBooking(Booking booking) {
        return bookingHistoryRepository.findByBooking(booking).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Can them CRUD tự làm nha ae :]]

    private BookingHistoryDTO convertToDTO(BookingHistory bookingHistory) {
        return new BookingHistoryDTO(
                bookingHistory.getHistoryId(),
                bookingHistory.getBooking().getBookingId(),
                bookingHistory.getUser().getId(),
                bookingHistory.getStatus(),
                bookingHistory.getChangeDate()
        );
    }
}

