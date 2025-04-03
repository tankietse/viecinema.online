package com.viecinema.moviebooking.repository;

import java.util.List;
import com.viecinema.moviebooking.model.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatTypeRepository extends JpaRepository<SeatType, Integer> {
    boolean existsByTypeName(String typeName);
    /**
     * Find seat types used in a specific theater
     */
    @Query("SELECT DISTINCT st FROM Screen scr JOIN scr.seat s JOIN s.seatType st WHERE scr.theater.id = :theaterId")
    List<SeatType> findByTheater(@Param("theaterId") Long theaterId);
    
    /**
     * Find seat types used with a specific screen type
     */
    @Query("SELECT DISTINCT st FROM Screen scr JOIN scr.seat s JOIN s.seatType st WHERE scr.screenType.id = :screenTypeId")
    List<SeatType> findByScreenType(@Param("screenTypeId") Integer screenTypeId);
    
    /**
     * Find seat types used in a specific theater with a specific screen type
     */
    @Query("SELECT DISTINCT st FROM Screen scr JOIN scr.seat s JOIN s.seatType st WHERE scr.theater.id = :theaterId AND scr.screenType.id = :screenTypeId")
    List<SeatType> findByTheaterAndScreenType(@Param("theaterId") Long theaterId, @Param("screenTypeId") Integer screenTypeId);
}