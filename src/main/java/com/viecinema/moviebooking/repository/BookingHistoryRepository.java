package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.Booking;
import com.viecinema.moviebooking.model.BookingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingHistoryRepository extends JpaRepository<BookingHistory, Integer> {

    List<BookingHistory> findByBooking(Booking booking);
}
