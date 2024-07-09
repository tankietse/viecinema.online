package com.viecinema.moviebooking.service;

import com.viecinema.moviebooking.model.Screening;
import com.viecinema.moviebooking.repository.ScreeningRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScreeningService {

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
            Logger log = null;
            log.error("Error fetching screenings:", e);
            throw new RuntimeException("Lỗi khi tìm nạp suất chieu", e);
        }
    }

}

