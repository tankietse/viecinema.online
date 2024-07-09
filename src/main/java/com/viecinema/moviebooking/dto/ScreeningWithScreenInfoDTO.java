package com.viecinema.moviebooking.dto;

import com.viecinema.moviebooking.model.Screening;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ScreeningWithScreenInfoDTO {
    private Long screeningId;
    private LocalDate screeningDate;
    private LocalTime startTime;
    private ScreenInfoDTO screenInfo;

    public ScreeningWithScreenInfoDTO(Screening screening, ScreenInfoDTO screenInfo) {
        this.screeningId = screening.getScreeningId();
        this.screeningDate = screening.getScreeningDate();
        this.startTime = screening.getStartTime();
        this.screenInfo = screenInfo;
    }

    public String getTheaterName() {
        return screenInfo.getTheaterName();
    }
}