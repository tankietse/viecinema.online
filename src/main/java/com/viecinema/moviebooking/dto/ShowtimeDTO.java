package com.viecinema.moviebooking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ShowtimeDTO {
    private Integer showtimeId;
    private Integer movieId;
    private Integer screenId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    //
    private String theaterName;
    private String screenName;
    private String screenType;
    private BigDecimal price;
}