package com.viecinema.moviebooking.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ShowtimeScreeningDTO {
    private int movie;
    private int showtime;
    private int screen;
    private LocalDate screeningDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
}
