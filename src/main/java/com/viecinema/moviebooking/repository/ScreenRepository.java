package com.viecinema.moviebooking.repository;

import com.viecinema.moviebooking.dto.ScreenInfoDTO;
import com.viecinema.moviebooking.model.Screen;
import com.viecinema.moviebooking.model.Theater;
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

    List<Screen> findByTheater(Theater theater);

}
