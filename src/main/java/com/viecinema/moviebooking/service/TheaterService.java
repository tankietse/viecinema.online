package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.model.Theater;
import com.viecinema.moviebooking.repository.TheaterRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TheaterService {

    private final TheaterRepository theaterRepository;

    public TheaterService(TheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
    }

    /**
     * Get all theaters
     */
    public List<Theater> findAll() {
        return theaterRepository.findAll();
    }

    /**
     * Get theaters with pagination
     */
    public Page<Theater> findAll(Pageable pageable) {
        return theaterRepository.findAll(pageable);
    }

    /**
     * Find theater by id
     */
    public Optional<Theater> findById(Integer id) {
        return theaterRepository.findById(id);
    }

    /**
     * Save or update a theater
     */
    public Theater save(Theater theater) {
        return theaterRepository.save(theater);
    }

    /**
     * Delete a theater by id
     */
    public void deleteById(Integer id) {
        theaterRepository.deleteById(id);
    }
    
    /**
     * Check if a theater exists by name
     */
    public boolean existsByTheaterName(String theaterName) {
        return theaterRepository.existsByTheaterName(theaterName);
    }

    /**
     * Get all theaters - API method
     */
    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }

    /**
     * Get theater by id - API method
     */
    public Optional<Theater> getTheaterById(Integer id) {
        return theaterRepository.findById(id);
    }

    /**
     * Get theater by name
     */
    public Optional<Theater> getTheaterByName(String name) {
        return theaterRepository.findByTheaterName(name);
    }

    /**
     * Save theater - API method
     */
    public Theater saveTheater(Theater theater) {
        return theaterRepository.save(theater);
    }

    /**
     * Delete theater - API method
     */
    public void deleteTheater(Integer id) {
        theaterRepository.deleteById(id);
    }

    /**
     * Find theaters by filters
     */
    public List<Theater> findAllByFilters(String name, String location, Pageable pageable) {
        List<Theater> allTheaters = theaterRepository.findAll();
        
        // Apply filters if provided
        if (name != null && !name.isEmpty()) {
            allTheaters = allTheaters.stream()
                .filter(theater -> theater.getTheaterName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        }
        
        if (location != null && !location.isEmpty()) {
            allTheaters = allTheaters.stream()
                .filter(theater -> theater.getAddress() != null && 
                       theater.getAddress().toLowerCase().contains(location.toLowerCase()))
                .collect(Collectors.toList());
        }
        
        // Apply sorting if specified in pageable
        if (pageable.getSort().isSorted()) {
            // Implementation depends on sort fields, this is just a placeholder
        }
        
        // Apply pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allTheaters.size());
        
        if (start >= allTheaters.size()) {
            return List.of();
        }
        
        return allTheaters.subList(start, end);
    }

    /**
     * Count all theaters
     */
    public long count() {
        return theaterRepository.count();
    }
}
