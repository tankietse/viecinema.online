package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.model.SeatType;
import org.springframework.beans.factory.annotation.Autowired;

import com.viecinema.moviebooking.repository.SeatTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    
    public boolean existsByTypeName(String typeName) {
        return seatTypeRepository.existsByTypeName(typeName);
    }
    
    public SeatType save(SeatType seatType) {
        return seatTypeRepository.save(seatType);
    }
    
    public void deleteById(Integer id) {
        seatTypeRepository.deleteById(id);
    }
    
    public Optional<SeatType> findById(Integer id) {
        return seatTypeRepository.findById(id);
    }

    public List<SeatType> findAll() {
        return seatTypeRepository.findAll();
    }

    public List<SeatType> findByScreenType(Integer screenTypeId) {
        return seatTypeRepository.findByScreenType(screenTypeId);
    }

    public SeatType findByIdOrNull(Integer id) {
        return seatTypeRepository.findById(id).orElse(null);
    }

    public boolean existsById(Integer id) {
        return seatTypeRepository.existsById(id);
    }

    public List<SeatType> findByTheater(Long theaterId) {
        return seatTypeRepository.findByTheater(theaterId);
    }
}
