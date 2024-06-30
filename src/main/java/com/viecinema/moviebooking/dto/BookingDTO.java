package com.viecinema.moviebooking.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingDTO {
    private Integer id;
    private Integer userId;
    private Integer movieId;
    private LocalDateTime bookingTime;
    private LocalDateTime showTime;
    private String seat;
}
