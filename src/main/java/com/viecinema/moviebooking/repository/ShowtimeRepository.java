package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.model.Showtime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    List<Showtime> findByMovieId(Long movieId);
    
    @Query("SELECT DISTINCT s FROM Showtime s JOIN s.screenings scr JOIN scr.screen sc JOIN sc.theater t " +
           "WHERE (:movieId IS NULL OR s.movie.id = :movieId) " +
           "AND (:theaterId IS NULL OR t.theaterId = :theaterId) " +
           "AND (:startDate IS NULL OR s.endDate >= :startDate) " +
           "AND (:endDate IS NULL OR s.startDate <= :endDate)")
    Page<Showtime> findShowtimes(
            @Param("movieId") Long id, 
            @Param("theaterId") Long theaterId, 
            @Param("startDate") LocalDate startDate, 
            @Param("endDate") LocalDate endDate, 
            Pageable pageable);
//    @Query("SELECT new com.viecinema.moviebooking.dto.ShowtimeDTO(" +
//            "s.startTime.toLocalDate(), s.startTime.toLocalTime(), t.theaterName, sc.screenName, sc.screenType, st.price) " +
//            "FROM Showtime s " +
//            "JOIN s.movie m " +
//            "JOIN s.screen sc " +
//            "JOIN s.theater t " +
//            "JOIN SeatType st ON sc.seatTypeId = st.seatTypeId " +
//            "WHERE m.movieId = :movieId")
//    List<ShowtimeDTO> findShowtimeDTOsByMovieId(@Param("movieId") Integer movieId);
}
