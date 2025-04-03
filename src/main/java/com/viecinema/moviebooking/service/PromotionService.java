package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.model.Promotion;
import com.viecinema.moviebooking.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {
    
    @Autowired
    private PromotionRepository promotionRepository;
    
    /**
     * Get all promotions
     */
    public List<Promotion> findAll() {
        return promotionRepository.findAll();
    }
    
    /**
     * Get promotions with pagination
     */
    public Page<Promotion> findAll(Pageable pageable) {
        return promotionRepository.findAll(pageable);
    }
    
    /**
     * Find active promotions
     */
    public List<Promotion> findActivePromotions() {
        LocalDate today = LocalDate.now();
        return promotionRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(today, today);
    }
    
    /**
     * Find promotion by ID
     */
    public Optional<Promotion> findById(Integer id) {
        return promotionRepository.findById(id);
    }
    
    /**
     * Find promotion by code
     */
    public Optional<Promotion> findByCode(String code) {
        return promotionRepository.findByCode(code);
    }
    
    /**
     * Save or update promotion
     */
    public Promotion save(Promotion promotion) {
        return promotionRepository.save(promotion);
    }
    
    /**
     * Delete promotion by ID
     */
    public void deleteById(Integer id) {
        promotionRepository.deleteById(id);
    }
    
    /**
     * Check if promotion code exists
     */
    public boolean existsByCode(String code) {
        return promotionRepository.existsByCode(code);
    }
}
