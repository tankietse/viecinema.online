package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    Optional<Promotion> findByCode(String code);
    
    boolean existsByCode(String code);
    
    List<Promotion> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate currentDate, LocalDate sameDate);
}
