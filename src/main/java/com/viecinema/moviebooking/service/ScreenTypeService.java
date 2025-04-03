package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.model.ScreenType;
import com.viecinema.moviebooking.repository.ScreenTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScreenTypeService {
    
    @Autowired
    private ScreenTypeRepository screenTypeRepository;
    
    /**
     * Get all screen types
     */
    public List<ScreenType> findAll() {
        return screenTypeRepository.findAll();
    }
    
    /**
     * Get screen types with pagination
     */
    public Page<ScreenType> findAll(Pageable pageable) {
        return screenTypeRepository.findAll(pageable);
    }
    
    /**
     * Find screen type by ID
     */
    public Optional<ScreenType> findById(Integer id) {
        return screenTypeRepository.findById(id);
    }
    
    /**
     * Save or update screen type
     */
    public ScreenType save(ScreenType screenType) {
        return screenTypeRepository.save(screenType);
    }
    
    /**
     * Delete screen type by ID
     */
    public void deleteById(Integer id) {
        screenTypeRepository.deleteById(id);
    }
    
    /**
     * Check if screen type name exists
     */
    public boolean existsByTypeName(String typeName) {
        return screenTypeRepository.existsByTypeName(typeName);
    }
}
