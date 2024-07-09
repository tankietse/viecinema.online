package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Integer> {
    @Query("SELECT COUNT(s) FROM Screening s WHERE s.showtime.showtimeId = :showtimeId " +
            "AND s.screeningDate = :screeningDate " +
            "AND ((:startTime BETWEEN s.startTime AND s.endTime) " +
            "OR (:endTime BETWEEN s.startTime AND s.endTime) " +
            "OR (s.startTime BETWEEN :startTime AND :endTime) " +
            "OR (s.endTime BETWEEN :startTime AND :endTime))")
    long countOverlappingScreenings(@Param("showtimeId") Integer showtimeId,
                                    @Param("screeningDate") LocalDate screeningDate,
                                    @Param("startTime") LocalTime startTime,
                                    @Param("endTime") LocalTime endTime);
    // đảm bảo không có sự chồng chéo về thời gian chiếu khi thêm các suất chiếu mới vào DB

    List<Screening> findByShowtimeShowtimeId(Integer showtimeId);
    List<Screening> findByScreeningDate(LocalDate screeningDate);
    List<Screening> findByShowtimeShowtimeIdAndScreeningDate(Integer showtimeId, LocalDate date);
}

