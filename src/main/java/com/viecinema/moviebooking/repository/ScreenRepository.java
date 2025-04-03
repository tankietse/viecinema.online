package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.dto.ScreenInfoDTO;
import com.viecinema.moviebooking.model.Screen;
import com.viecinema.moviebooking.model.Theater;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Integer> {
    @Query("SELECT new com.viecinema.moviebooking.dto.ScreenInfoDTO(s.name, t.theaterName, t.address, s.capacity, sl.rowNumber, sl.seatNumber, st.typeName, seatType.typeName, st.screenPrice, seatType.seatPrice) " +
            "FROM Screen s " +
            "JOIN s.theater t " +
            "JOIN s.screenType st " +
            "JOIN s.seat sl " +
            "JOIN sl.seatType seatType " +
            "WHERE s.id = :screenId")
    ScreenInfoDTO findByScreenId(@Param("screenId") Integer screenId);

    List<Screen> findByTheater(@Param("theaterId") Theater theater);
    
    /**
     * Check if a screen with the given name exists in the theater
     */
    boolean existsByTheaterAndName(Theater theater, String name);

    /**
     * Find screens by theater ID and screen type ID
     * Handles null parameters for flexible filtering
     */
    @Query("SELECT s FROM Screen s WHERE (:theaterId IS NULL OR s.theater.theaterId = :theaterId) AND (:screenTypeId IS NULL OR s.screenType.screenTypeId = :screenTypeId)")
    Page<Screen> findByTheaterAndScreenType(@Param("theaterId") Integer theaterId, @Param("screenTypeId") Integer screenTypeId, Pageable pageable);

    /**
     * Find screens by theater ID and seat type ID
     */
    @Query("SELECT s FROM Screen s WHERE s.theater.id = :theaterId AND s.seat.seatType.id = :seatTypeId")
    Page<Screen> findByTheaterAndSeatType(@Param("theaterId") Integer theaterId, @Param("seatTypeId") Integer seatTypeId, Pageable pageable);
}
