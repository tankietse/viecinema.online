package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.dto.ShowtimeDTO;
import com.viecinema.moviebooking.model.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Integer> {
    List<Showtime> findByMovieId(Integer movieId);

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
