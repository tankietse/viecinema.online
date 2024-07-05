package com.viecinema.moviebooking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleDTO {
    private Integer scheduleId;
    private Integer movieId;
    private Integer screenId;
    private Integer theaterId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // Constructor
    public ScheduleDTO(Integer scheduleId, Integer movieId, Integer screenId, Integer theaterId, LocalDateTime startTime, LocalDateTime endTime) {
        this.scheduleId = scheduleId;
        this.movieId = movieId;
        this.screenId = screenId;
        this.theaterId = theaterId;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
