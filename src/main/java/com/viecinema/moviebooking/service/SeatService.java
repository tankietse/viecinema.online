package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.model.Seat;
import com.viecinema.moviebooking.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SeatService {
    @Autowired
    private SeatRepository seatRepository;

    public Optional<Seat> getSeatsByScreenId(int screenId) {
        return seatRepository.findById(screenId);
    }
}
