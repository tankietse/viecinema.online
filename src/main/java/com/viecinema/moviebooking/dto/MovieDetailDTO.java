package com.viecinema.moviebooking.dto;

import com.viecinema.moviebooking.model.*;

import java.util.List;

public class MovieDetailDTO {
    private MovieDTO movie;
    private List<ShowtimeDTO> showtimes;
    private List<Screening> screenings;
    private List<Screen> screens;
    private List<ScreenType> screenTypes;
    private List<Seat> seats;
    private List<SeatType> seatTypes;
    private List<Theater> theaters;
}
