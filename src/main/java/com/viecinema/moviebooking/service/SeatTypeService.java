package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.model.SeatType;
import org.springframework.beans.factory.annotation.Autowired;

import com.viecinema.moviebooking.repository.SeatTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatTypeService {
    private final SeatTypeRepository seatTypeRepository;

    @Autowired
    public SeatTypeService(SeatTypeRepository seatTypeRepository) {
        this.seatTypeRepository = seatTypeRepository;
    }

    public List<SeatType> getAllSeatTypes() {
        return seatTypeRepository.findAll();
    }
}
