package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.model.Screening;
import com.viecinema.moviebooking.repository.ScreeningRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScreeningService {
    
    private static final Logger log = LoggerFactory.getLogger(ScreeningService.class);

    @Autowired
    private ScreeningRepository screeningRepository;

    public Screening addScreening(Screening screening) {
        long overlappingCount = screeningRepository.countOverlappingScreenings(
                screening.getShowtime().getShowtimeId(),
                screening.getScreeningDate(),
                screening.getStartTime(),
                screening.getEndTime()
        );

        if (overlappingCount > 0) {
            throw new IllegalArgumentException("Lỗi! Có sự trùng lắp thời gian của các suất chiếu!");
        }
        return screeningRepository.save(screening);
    }

    public List<Screening> getScreeningsByShowtimeId(Integer showtimeId) {
        return screeningRepository.findByShowtimeShowtimeId(showtimeId);
    }

    public List<Screening> getScreeningsByDate(LocalDate screeningDate) {
        return screeningRepository.findByScreeningDate(screeningDate);
    }
    
    public List<Screening> getScreeningsByShowtimeIdAndDate(Integer showtimeId, LocalDate date) {
        try {
            return screeningRepository.findByShowtimeShowtimeIdAndScreeningDate(showtimeId, date);
        } catch (Exception e) {
            log.error("Error fetching screenings: {}", e.getMessage(), e);
            throw new RuntimeException("Lỗi khi tìm nạp suất chieu", e);
        }
    }
}

